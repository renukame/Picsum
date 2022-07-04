package com.teasers.picsum.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teasers.picsum.data.local.db.PicSumDatabase
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.data.remote.api.PicSumService
import com.teasers.picsum.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public const val PAGE_SIZE = 20

class RepositoryImpl @Inject constructor(
    private val service: PicSumService,
    private val database: PicSumDatabase
) : Repository {
    override fun getAll(): Flow<PagingData<PicSum>> {
        /*
        // for paging source
         @OptIn(ExperimentalPagingApi::class)
         return Pager(
             config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
             pagingSourceFactory = { PicSumPagingSource(service) }
         ).flow*/

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PicSumRemoteMediator(
                service,
                database
            ),
            pagingSourceFactory = { database.picSumDao().getAll() }
        ).flow
    }


}