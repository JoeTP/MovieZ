package com.example.feature_movie_details.presentation

import com.example.domain.model.MovieDetails

interface MovieDetailsContract {

    sealed interface Intent{
        data class Load(val id: Int): Intent
        data class Retry(val id: Int): Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val movieDetails: MovieDetails? = null,
        val error: String? = null,
    )

    sealed interface Effect{
        data class ShowMessage(val msg: String): Effect
    }
}