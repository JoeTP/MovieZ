package com.example.khatibalamytask.data.remote

import com.example.core.network.dto.MovieDetailsDto
import com.example.core.network.response.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {
    //docs https://developer.themoviedb.org/reference/movie-popular-list
    //TODO: modify the queries to order the movies
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
    ): Response<MoviesResponse>

    //docs https://developer.themoviedb.org/reference/search-movie
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
    ): Response<MoviesResponse>

    //docs https://developer.themoviedb.org/reference/movie-details
    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): Response<MovieDetailsDto>

}