package com.teasers.picsum.imageloader

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.teasers.picsum.BuildConfig

@GlideModule
class GlideModule : AppGlideModule() {
    @Override
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        if (BuildConfig.DEBUG)
            builder.setLogLevel(Log.VERBOSE)
    }
}