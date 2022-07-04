package com.teasers.picsum.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teasers.picsum.data.model.PicSum

@Database(entities = [PicSum::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class PicSumDatabase : RoomDatabase() {
    abstract fun picSumDao(): PicSumDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}