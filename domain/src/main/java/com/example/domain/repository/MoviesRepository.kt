package com.example.domain.repository

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getMovies(): Flow<ResultState<List<Movie>>>

    fun search(query: String): Flow<ResultState<List<Movie>>>

    suspend fun getMovieById(id: Int): ResultState<Movie>
}