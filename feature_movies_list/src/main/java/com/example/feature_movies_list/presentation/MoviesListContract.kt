package com.example.feature_movies_list.presentation

import com.example.domain.model.Movie


interface MovieListContract {
    sealed interface Intent {
        data object Load : Intent
        data object Retry : Intent
        data class OpenDetails(val id: Int) : Intent
        data class Search(val query: String) : Intent
        data object LoadNextPage : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val error: String? = null,
        val isEmpty: Boolean = false,
        val isLoadingNextPage: Boolean = false
    )

    sealed interface Effect {
        data class ShowMessage(val msg: String) : Effect
        data class NavigateToDetails(val id: Int) : Effect
        data class OpenSearch(val query: String) : Effect
    }
}
