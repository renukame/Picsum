package com.teasers.picsum.di

import android.content.Context
import androidx.room.Room
import com.teasers.picsum.data.local.db.PicSumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DB_NAME = "picsum_db"

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): PicSumDatabase {
        return Room.databaseBuilder(context, PicSumDatabase::class.java, DB_NAME)
            .build()
    }
}