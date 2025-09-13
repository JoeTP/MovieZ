package com.example.khatibalamytask.data.remote

import com.example.core.network.dto.MovieDetailsDto
import com.example.core.network.response.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApiService {
    companion object {
        //This wasn't intended but I had to put it as a fast solution to filter uncensored movies
        private val filter = arrayOf(
            mapOf("id" to 28, "name" to "Action"),
            mapOf("id" to 12, "name" to "Adventure"),
            mapOf("id" to 16, "name" to "Animation"),
            mapOf("id" to 35, "name" to "Comedy"),
            mapOf("id" to 80, "name" to "Crime"),
            mapOf("id" to 99, "name" to "Documentary"),
//    mapOf("id" to 18, "name" to "Drama"),
            mapOf("id" to 10751, "name" to "Family"),
            mapOf("id" to 14, "name" to "Fantasy"),
            mapOf("id" to 36, "name" to "History"),
            mapOf("id" to 27, "name" to "Horror"),
            mapOf("id" to 10402, "name" to "Music"),
            mapOf("id" to 9648, "name" to "Mystery"),
//    mapOf("id" to 10749, "name" to "Romance"),
            mapOf("id" to 878, "name" to "Science Fiction"),
            mapOf("id" to 10770, "name" to "TV Movie"),
            mapOf("id" to 53, "name" to "Thriller"),
            mapOf("id" to 10752, "name" to "War"),
            mapOf("id" to 37, "name" to "Western")
        )
    }

    //docs https://developer.themoviedb.org/reference/movie-popular-list
    //TODO: modify the queries to order the movies
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
//        @Query("genre_ids") genreIds: Array<out Map<String, Any>> = filter,
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