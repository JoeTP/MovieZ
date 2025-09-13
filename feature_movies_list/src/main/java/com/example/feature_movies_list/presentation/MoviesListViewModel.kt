package com.example.feature_movies_list.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.result_states.ResultState
import com.example.domain.usecases.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
) : ViewModel(), MovieListContract {

    private val _state = MutableStateFlow(MovieListContract.State())
    val state: StateFlow<MovieListContract.State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<MovieListContract.Effect>()
    val effects: SharedFlow<MovieListContract.Effect> = _effects

    private val intents = MutableSharedFlow<MovieListContract.Intent>()

    private var currentPage = 1
    private var isLoadingNextPage = false
    private var maxPage = 500

    init {
        processIntents()
    }

    fun sendIntent(i: MovieListContract.Intent) = viewModelScope.launch { intents.emit(i) }


    private fun loadMovies() = viewModelScope.launch {
        getMoviesUseCase(currentPage)
            .onEach { result ->
                _state.update {
                    Log.d("TAG", "loadMovies: $result")
                    when (result) {
                        is ResultState.Loading -> it.copy(
                            isLoading = true,
                            error = null,
                            isLoadingNextPage = false
                        )

                        is ResultState.Success -> {
                            Log.i("MAX", "before: $maxPage")
                            maxPage = result.data.totalPages
                            Log.i("MAX", "after: $maxPage")
                            it.copy(
                                isLoading = false,
                                movies = result.data.movies,
                                isEmpty = result.data.movies.isEmpty(),
                                error = null,
                                isLoadingNextPage = false
                            )
                        }

                        is ResultState.Error -> it.copy(
                            isLoading = false,
                            error = result.message,
                            isLoadingNextPage = false
                        )
                    }

                }
            }
            .catch { e ->
                val msg = e.message ?: "Unexpected error"
                _state.update { it.copy(isLoading = false, error = msg) }
                _effects.emit(MovieListContract.Effect.ShowMessage(msg))
            }.collect()
    }

    private fun loadNextPage() {
        if (isLoadingNextPage || currentPage >= maxPage) return
        isLoadingNextPage = true
        _state.update { it.copy(isLoadingNextPage = true) }
        viewModelScope.launch {
            getMoviesUseCase(currentPage + 1).onEach { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _state.update { it.copy(isLoadingNextPage = true) }
                    }

                    is ResultState.Success -> {
                        _state.update { prev ->
                            val newMovies = result.data.movies
                            val allMovies = prev.movies + newMovies
                            prev.copy(
                                movies = allMovies,
                                isLoading = false,
                                error = null,
                                isLoadingNextPage = false,
                                isEmpty = allMovies.isEmpty()
                            )
                        }
                        currentPage++
                        if (currentPage >= /*maxPage*/ 500) {
                            _effects.emit(MovieListContract.Effect.ShowMessage("Free Limit Reached"))
                        }
                        isLoadingNextPage = false
                    }

                    is ResultState.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message,
                                isLoadingNextPage = false
                            )
                        }
                        _effects.emit(MovieListContract.Effect.ShowMessage(result.message))
                        isLoadingNextPage = false
                    }
                }
            }.catch { e ->
                val msg = e.message ?: "Unexpected error"
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = msg,
                        isLoadingNextPage = false
                    )
                }
                _effects.emit(MovieListContract.Effect.ShowMessage(msg))
                isLoadingNextPage = false
            }.collect()
        }
    }

    private fun processIntents() = viewModelScope.launch {
        intents.onEach { intent ->
            when (intent) {
                is MovieListContract.Intent.Retry, MovieListContract.Intent.Load,
                    -> {
                    currentPage = 1
                    _state.update { it.copy(movies = emptyList(), isEmpty = false, error = null) }
                    loadMovies()
                }

                is MovieListContract.Intent.OpenDetails -> {
                    _effects.emit(MovieListContract.Effect.NavigateToDetails(intent.id))
                }

                is MovieListContract.Intent.LoadNextPage -> loadNextPage()
            }
        }.collect()
    }
}
