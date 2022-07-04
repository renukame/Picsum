package com.teasers.picsum.data.remote.api

import com.teasers.picsum.data.model.PicSum
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PicSumService {

    @GET("v2/list")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<PicSum>>

}