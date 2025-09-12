package com.example.data.sources.remote

import com.example.core.network.dto.MovieDto
import com.example.core.network.response.MoviesResponse
import retrofit2.Response

interface MoviesRemoteDataSource {
    suspend fun fetchPopularMovies(page: Int = 1): Response<MoviesResponse> // Add page parameter
    suspend fun search(query: String, page: Int = 1): Response<MoviesResponse> // Add page parameter and fix return type
    suspend fun fetchMovie(id: Int): Response<MovieDto>
}