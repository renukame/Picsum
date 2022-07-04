package com.teasers.picsum.features

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.domain.usecases.GetAllPicSumUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PicSumViewModel @Inject constructor(private val getAllPicSumUsecase: GetAllPicSumUsecase) :
    ViewModel() {

    private val _selectedPicSum = MutableLiveData<PicSum>()

    fun fetchAllPicSums(): Flow<PagingData<PicSum>> {
        return getAllPicSumUsecase()
    }

    fun selectedPicSum(picSum: PicSum) {
        _selectedPicSum.value = picSum
    }

    fun getSelectedPicSum() = _selectedPicSum.value
}