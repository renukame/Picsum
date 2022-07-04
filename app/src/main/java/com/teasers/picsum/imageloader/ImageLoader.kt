package com.teasers.picsum.imageloader

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.teasers.picsum.R

object ImageLoader {

    fun loadImage(
        view: View,
        url: String,
        imageView: ImageView,
        errorDrawable: Int = R.drawable.error
    ) {
        Glide.with(view).load(url).error(errorDrawable)
            .into(imageView)
    }

    fun loadImage(
        fragment: Fragment,
        url: String,
        imageView: ImageView,
        errorDrawable: Int = R.drawable.error
    ) {
        Glide.with(fragment).load(url).error(errorDrawable).into(imageView)
    }
}