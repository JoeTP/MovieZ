package com.example.data.sources.remote

import com.example.core.network.dto.MovieDto
import com.example.khatibalamytask.data.remote.MoviesApiService
import retrofit2.Response
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(private val service: MoviesApiService) : MoviesRemoteDataSource {

    override suspend fun fetchPopularMovies(): Response<List<MovieDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun search(query: String): Response<List<MovieDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMovie(id: Int): Response<MovieDto> {
        TODO("Not yet implemented")
    }
}