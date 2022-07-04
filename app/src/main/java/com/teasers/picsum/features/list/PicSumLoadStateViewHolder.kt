package com.teasers.picsum.features.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.teasers.picsum.R
import com.teasers.picsum.databinding.PicsumLoadStateViewItemBinding

class PicSumLoadStateViewHolder(
    private val binding: PicsumLoadStateViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMessage.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PicSumLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.picsum_load_state_view_item, parent, false)
            val binding = PicsumLoadStateViewItemBinding.bind(view)
            return PicSumLoadStateViewHolder(binding, retry)
        }
    }
}