package com.teasers.picsum.domain.usecases

import androidx.paging.PagingData
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPicSumUsecase @Inject constructor(private val repository: Repository) {
     operator fun invoke(): Flow<PagingData<PicSum>> {
        return repository.getAll()
    }
}