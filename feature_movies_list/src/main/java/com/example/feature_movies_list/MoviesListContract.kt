package com.example.feature_movies_list

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie


interface MovieListContract {
    sealed interface Intent {
        data object Load : Intent
        data class Retry(val reason: String? = null) : Intent
        data class OpenDetails(val id: Int) : Intent
        data class Search(val query: String) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val error: String? = null,
        val isEmpty: Boolean = false
    )

    sealed interface Effect {
        data class ShowMessage(val msg: String) : Effect
        data class NavigateToDetails(val id: Int) : Effect
    }
}

