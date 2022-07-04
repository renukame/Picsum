package com.teasers.picsum.features.list

import androidx.recyclerview.widget.RecyclerView
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.databinding.PicsumItemsBinding
import com.teasers.picsum.imageloader.ImageLoader


class PicSumViewHolder(
    private val binding: PicsumItemsBinding,
    private val onItemClick: (picsum: PicSum) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(picsum: PicSum) {
        with(binding) {
            picsumAuthor.text = picsum.author
            ImageLoader.loadImage(root, picsum.download_url, picusmImage)
            picsumCard.setOnClickListener { onItemClick(picsum) }
        }
    }
}