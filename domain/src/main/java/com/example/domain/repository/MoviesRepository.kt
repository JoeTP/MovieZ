package com.example.domain.repository

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails
import com.example.domain.model.MoviesPage
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getMovies(page: Int = 1): Flow<ResultState<MoviesPage>>

    fun search(query: String, page: Int = 1): Flow<ResultState<MoviesPage>>

    suspend fun getMovieById(id: Int): Flow<ResultState<MovieDetails>>
}