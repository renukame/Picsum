package com.teasers.picsum.features.list

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class PicSumLoadStateAdapter(private val retry: () -> Unit
) : LoadStateAdapter<PicSumLoadStateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PicSumLoadStateViewHolder {
        return PicSumLoadStateViewHolder.create(parent, retry)
    }

    override fun onBindViewHolder(holder: PicSumLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}