package com.example.data.sources.local

import com.example.core.database.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MoviesLocalDataSource {
    suspend fun retrievePopularMovies(): Flow<List<MovieEntity>>
    suspend fun cacheAndUpdateMovies(movies: List<MovieEntity>)
}