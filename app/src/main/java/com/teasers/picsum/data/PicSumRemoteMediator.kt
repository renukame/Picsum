package com.teasers.picsum.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.teasers.picsum.data.local.db.PicSumDatabase
import com.teasers.picsum.data.local.db.RemoteKeys
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.data.remote.api.PicSumService
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class PicSumRemoteMediator(private val service: PicSumService, private val database: PicSumDatabase) :
    RemoteMediator<Int, PicSum>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PicSum>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }

            LoadType.PREPEND
            -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
        }

        try {
            val response = service.getPhotos(page, state.config.pageSize)
            val picusmList = response.body()
            val endOfPagination = picusmList!!.isEmpty()

            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.picSumDao().clearAll()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val keys = picusmList.map {
                    RemoteKeys(picsumId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.picSumDao().insertAll(picusmList)
                database.remoteKeysDao().insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, PicSum>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()
            ?.let { picsum -> database.remoteKeysDao().remoteKeysPicsumId(picsum.id) }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PicSum>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                // Get the remote keys of the first items retrieved
                database.remoteKeysDao().remoteKeysPicsumId(it.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PicSum>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { pisumId ->
                database.remoteKeysDao().remoteKeysPicsumId(pisumId)
            }
        }
    }
}