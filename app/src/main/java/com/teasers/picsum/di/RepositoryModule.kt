package com.teasers.picsum.di

import com.teasers.picsum.data.RepositoryImpl
import com.teasers.picsum.data.RepositoryImpl_Factory
import com.teasers.picsum.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun picSumRepository(repository: RepositoryImpl): Repository
}
