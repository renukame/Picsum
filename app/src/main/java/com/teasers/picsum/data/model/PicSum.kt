package com.teasers.picsum.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "picsum")
data class PicSum(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("download_url")
    val download_url: String
)