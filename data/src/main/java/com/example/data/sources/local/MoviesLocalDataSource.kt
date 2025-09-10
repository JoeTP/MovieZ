package com.example.data.sources.local

import com.example.core.database.entities.MovieEntity
import com.example.core.network.dto.MovieDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MoviesLocalDataSource {
    suspend fun retrievePopularMovies(): Flow<List<MovieEntity>>
    suspend fun updateMovies(movies: List<MovieEntity>)
}