package com.example.moviez.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRouts {

    @Serializable
    object MoviesList : AppRouts()
    @Serializable
    object MovieListSearch : AppRouts()
    @Serializable
    data class MovieDetails(val id : Int) : AppRouts()

}