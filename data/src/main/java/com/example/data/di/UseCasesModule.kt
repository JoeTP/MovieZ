package com.example.data.di

import com.example.domain.repository.MoviesRepository
import com.example.domain.usecases.GetMovieDetailsUseCase
import com.example.domain.usecases.GetMoviesUseCase
import com.example.domain.usecases.SearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(repo: MoviesRepository): GetMoviesUseCase {
        return GetMoviesUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(repo: MoviesRepository): SearchMoviesUseCase {
        return SearchMoviesUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailsUseCase(repo: MoviesRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(repo)
    }
}