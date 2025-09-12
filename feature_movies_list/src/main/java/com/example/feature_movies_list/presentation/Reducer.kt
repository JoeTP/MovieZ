package com.example.feature_movies_list.presentation

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie


fun MovieListContract.State.reduce(result: ResultState<List<Movie>>): MovieListContract.State =
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
