package com.example.data.di

import com.example.domain.repository.MoviesRepository
import com.example.domain.usecases.GetMoviesUseCase
import com.example.domain.usecases.SearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideGetPopularMoviesUseCase(repo: MoviesRepository): GetMoviesUseCase {
        return GetMoviesUseCase(repo)
    }

    @Provides
    fun provideSearchMoviesUseCase(repo: MoviesRepository): SearchMoviesUseCase {
        return SearchMoviesUseCase(repo)
    }
}