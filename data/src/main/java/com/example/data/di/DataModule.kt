package com.example.data.di

import com.example.data.repository.MoviesRepositoryImpl
import com.example.data.sources.local.MoviesLocalDataSource
import com.example.data.sources.local.MoviesLocalDataSourceImpl
import com.example.data.sources.remote.MoviesRemoteDataSource
import com.example.data.sources.remote.MoviesRemoteDataSourceImpl
import com.example.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindRemote(impl: MoviesRemoteDataSourceImpl): MoviesRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLocal(impl: MoviesLocalDataSourceImpl): MoviesLocalDataSource

    @Binds
    @Singleton
    abstract fun bindRepo(impl: MoviesRepositoryImpl): MoviesRepository
}
