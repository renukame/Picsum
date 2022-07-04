package com.teasers.picsum.features.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.databinding.PicsumItemsBinding

class PicSumAdapter(private val onItemClick: (picsum: PicSum) -> Unit) :
    PagingDataAdapter<PicSum, PicSumViewHolder>(PICSUM_COMPARATOR) {

    private lateinit var binding: PicsumItemsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicSumViewHolder {
        binding = PicsumItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PicSumViewHolder(binding, onItemClick)
    }


    override fun onBindViewHolder(holder: PicSumViewHolder, position: Int) {
        val picsum = getItem(position)
        picsum?.let { (holder as PicSumViewHolder).bind(it) }
    }

    companion object {
        private val PICSUM_COMPARATOR = object : DiffUtil.ItemCallback<PicSum>() {
            override fun areItemsTheSame(oldItem: PicSum, newItem: PicSum): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PicSum, newItem: PicSum): Boolean {
                return oldItem == newItem
            }
        }
    }
}