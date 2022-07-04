package com.teasers.picsum.data


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.data.remote.api.PicSumService
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 0

class PicSumPagingSource(private val service: PicSumService) : PagingSource<Int, PicSum>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PicSum> {
        val position = params.key ?: STARTING_PAGE_INDEX
        try {
            val response = service.getPhotos(position, params.loadSize)
            if (response.isSuccessful) {
                val picSumList = response.body()
                if (picSumList != null) {
                    val nextKey = if (picSumList.isEmpty()) {
                        null
                    } else {
                        // initial load size = 3 * NETWORK_PAGE_SIZE
                        if (params.loadSize > PAGE_SIZE)
                            position + (params.loadSize / PAGE_SIZE)
                        else
                            position + 1
                    }
                    return LoadResult.Page(
                        data = picSumList,
                        prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = nextKey
                    )
                }
            }
            return LoadResult.Error(Exception(response.errorBody().toString()))
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PicSum>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}