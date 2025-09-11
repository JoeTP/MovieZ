package com.example.feature_movies_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.result_states.userMessage
import com.example.domain.usecases.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel(), MovieListContract {

    private val _state = MutableStateFlow(MovieListContract.State())
    val state: StateFlow<MovieListContract.State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<MovieListContract.Effect>()
    val effects: SharedFlow<MovieListContract.Effect> = _effects

    private val intents = MutableSharedFlow<MovieListContract.Intent>()

    init {
        processIntents()
        intent(MovieListContract.Intent.Load)
    }

    fun intent(i: MovieListContract.Intent) = viewModelScope.launch { intents.emit(i) }


    private fun processIntents() {
        intents.onEach { intent ->
            when (intent) {
                MovieListContract.Intent.Load,
                is MovieListContract.Intent.Retry -> loadMovies()

                is MovieListContract.Intent.OpenDetails ->
                    _effects.emit(MovieListContract.Effect.NavigateToDetails(intent.id))

                is MovieListContract.Intent.Search -> {
                    // delegate to Search feature via navigation, or local filter if you want:
                    // _effects.emit(MovieListContract.Effect.OpenSearch)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadMovies() = viewModelScope.launch {
        getMoviesUseCase(Unit)
            .onEach { result -> _state.update {
                Log.d("TAG", "loadMovies: $result")
                it.reduce(result)
            } }
            .catch { e ->
                val msg = e.message ?: "Unexpected error"
                _state.update { it.copy(isLoading = false, error = msg) }
                _effects.emit(MovieListContract.Effect.ShowMessage(msg))
            }.collect()
    }
}
