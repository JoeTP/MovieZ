package com.example.data.sources.remote

import com.example.core.network.dto.MovieDto
import retrofit2.Response

interface MoviesRemoteDataSource {
    suspend fun fetchPopularMovies(): Response<List<MovieDto>>
    suspend fun search(query: String): Response<List<MovieDto>>
    suspend fun fetchMovie(id: Int): Response<MovieDto>
}