package com.example.data.sources.remote

import com.example.core.network.dto.MovieDto
import com.example.core.network.response.MoviesResponse
import retrofit2.Response

interface MoviesRemoteDataSource {
    suspend fun fetchPopularMovies(): Response<MoviesResponse>
    suspend fun search(query: String): Response<List<MovieDto>>
    suspend fun fetchMovie(id: Int): Response<MovieDto>
}