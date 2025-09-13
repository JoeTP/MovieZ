package com.example.feature_search.presentation

import com.example.domain.model.Movie

interface MoviesSearchListContract {

    sealed interface Intent {
        data class SearchMovies(val query: String) : Intent
        data class OpenDetails(val id: Int) : Intent
        data object LoadNextPage : Intent

    }

    data class State(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val isEmpty: Boolean = false,
        val error: String? = null,
        val isLoadingNextPage: Boolean = false,
    )

    sealed interface Effect {
        data class ShowMessage(val msg: String) : Effect
        data class NavigateToDetails(val id: Int) : Effect
    }

}