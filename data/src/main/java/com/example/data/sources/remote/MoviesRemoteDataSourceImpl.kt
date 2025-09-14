package com.example.data.sources.remote

import com.example.core.network.dto.MovieDetailsDto
import com.example.core.network.response.MoviesResponse
import com.example.khatibalamytask.data.remote.MoviesApiService
import retrofit2.Response
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(private val service: MoviesApiService) : MoviesRemoteDataSource {

    override suspend fun fetchPopularMovies(page: Int): Response<MoviesResponse> = service.getPopularMovies(page)

    override suspend fun search(query: String, page: Int): Response<MoviesResponse> = service.searchMovies(query, page)

    override suspend fun fetchMovie(id: Int): Response<MovieDetailsDto> = service.getMovieDetails(id)
}