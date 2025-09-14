package com.example.feature_movie_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.result_states.ResultState
import com.example.domain.usecases.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val getMovieDetailsUseCase: GetMovieDetailsUseCase) :
    ViewModel(), MovieDetailsContract {

    private val _state = MutableStateFlow(MovieDetailsContract.State())
    val state: StateFlow<MovieDetailsContract.State> = _state.asStateFlow()

    private val intents = MutableSharedFlow<MovieDetailsContract.Intent>()

    init {
        processIntent()
    }

    fun sendIntent(i: MovieDetailsContract.Intent) = viewModelScope.launch { intents.emit(i) }


    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        getMovieDetailsUseCase(movieId).onEach { result ->
            _state.update {
                when (result) {
                    is ResultState.Error -> it.copy(
                        isLoading = false,
                        error = result.message,
                    )

                    ResultState.Loading -> it.copy(
                        isLoading = true,
                        error = null,
                        movieDetails = null
                    )

                    is ResultState.Success -> it.copy(
                        isLoading = false,
                        error = null,
                        movieDetails = result.data
                    )
                }
            }
        }.collect()
    }

    private fun processIntent() = viewModelScope.launch {
        intents.onEach { intent ->
            when (intent) {
                is MovieDetailsContract.Intent.Load -> getMovieDetails(intent.id)
                is MovieDetailsContract.Intent.Retry -> getMovieDetails(intent.id)
            }
        }.collect()
    }

}