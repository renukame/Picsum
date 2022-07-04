package com.teasers.picsum.di


import com.teasers.picsum.BuildConfig
import com.teasers.picsum.data.remote.api.PicSumService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    private const val BASE_URL = "https://picsum.photos/"


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(interceptor)
        }
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build()
    }

    @Provides
    fun providePicsumService(retrofit: Retrofit): PicSumService {
        return retrofit.create(PicSumService::class.java)
    }


}