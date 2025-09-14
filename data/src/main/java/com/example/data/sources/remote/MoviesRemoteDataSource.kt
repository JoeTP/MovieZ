package com.example.data.sources.remote

import com.example.core.network.dto.MovieDetailsDto
import com.example.core.network.response.MoviesResponse
import retrofit2.Response

interface MoviesRemoteDataSource {
    suspend fun fetchPopularMovies(page: Int = 1): Response<MoviesResponse>
    suspend fun search(query: String, page: Int = 1): Response<MoviesResponse>
    suspend fun fetchMovie(id: Int): Response<MovieDetailsDto>
}