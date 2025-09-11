package com.example.moviez.navigation

import kotlinx.serialization.Serializable

sealed class AppRouts {

    @Serializable
    object MoviesList : AppRouts()
    @Serializable
    object MovieListSearch : AppRouts()
    @Serializable
    object MovieDetails : AppRouts()

}