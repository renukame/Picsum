package com.teasers.picsum.domain.repository

import androidx.paging.PagingData
import com.teasers.picsum.data.model.PicSum
import kotlinx.coroutines.flow.Flow

interface Repository {
     fun getAll(): Flow<PagingData<PicSum>>
}