package com.example.feature_search.presentation

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie


fun MoviesSearchListContract.State.reduce(result: ResultState<List<Movie>>): MoviesSearchListContract.State =
    when (result) {
        is ResultState.Loading -> copy(isLoading = true, error = null)
        is ResultState.Success -> copy(
            isLoading = false,
            movies = result.data,
            isEmpty = result.data.isEmpty(),
            error = null
        )
        is ResultState.Error -> copy(isLoading = false, error = result.message)
    }
