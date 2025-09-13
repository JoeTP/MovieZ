package com.example.feature_search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.result_states.ResultState
import com.example.domain.usecases.SearchMoviesUseCase
import com.example.domain.usecases.SearchMoviesUseCaseParams
import com.example.feature_search.presentation.MoviesSearchListContract.Effect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesSearchListViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
) : ViewModel(), MoviesSearchListContract {

    private val _state = MutableStateFlow(MoviesSearchListContract.State())
    val state: StateFlow<MoviesSearchListContract.State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<MoviesSearchListContract.Effect>()
    val effects: SharedFlow<MoviesSearchListContract.Effect> = _effects.asSharedFlow()
    private val searchQuery = MutableStateFlow("")
    private var currentPage = 1
    private var isLoadingNextPage = false
    private var maxPage = 500

    private val intents = MutableSharedFlow<MoviesSearchListContract.Intent>()


    init {
        processIntents()
        observeSearch()
    }


    fun sendIntent(intent: MoviesSearchListContract.Intent) =
        viewModelScope.launch { intents.emit(intent) }

    private fun observeSearch() = viewModelScope.launch {
        searchQuery
            .debounce(700L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                /**
                 * To cancel prev flows when query changes
                 * */
                val params = SearchMoviesUseCaseParams(query, currentPage)
                searchMoviesUseCase(params)
            }
            .onEach { result ->
                when (result) {
                    is ResultState.Success -> {
                        maxPage = result.data.totalPages
                        _state.update { prev ->
                            val movies = if (currentPage <= 1) result.data.movies else prev.movies
                            prev.copy(
                                isLoading = false,
                                movies = movies,
                                isEmpty = movies.isEmpty(),
                                error = null,
                                isLoadingNextPage = false
                            )
                        }
                    }

                    is ResultState.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is ResultState.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message, isLoadingNextPage = false) }
                    }
                }
            }
            .catch { e ->
                val msg = e.message ?: "Unexpected error"
                _effects.emit(ShowMessage(msg))
                _state.update { it.copy(isLoading = false, error = msg) }
            }.collect()
    }

    private fun loadNextPage() {
        if (isLoadingNextPage || currentPage >= maxPage || searchQuery.value.isBlank()) return
        isLoadingNextPage = true
        _state.update { it.copy(isLoadingNextPage = true) }
        viewModelScope.launch {
            val params = SearchMoviesUseCaseParams(searchQuery.value, currentPage + 1)
            searchMoviesUseCase(params).onEach { result ->
                when (result) {
                    is ResultState.Success -> {
//                        maxPage = result.data.totalPages
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
                        if (currentPage >= /*maxPage*/ 500) _effects.emit(ShowMessage("Free Limit Reached"))
                        isLoadingNextPage = false
                    }

                    is ResultState.Loading -> {
                        //already set isLoadingNextPage above
                    }

                    is ResultState.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message,
                                isLoadingNextPage = false
                            )
                        }
                        _effects.emit(ShowMessage(result.message))
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
                _effects.emit(ShowMessage(msg))
                isLoadingNextPage = false
            }.collect()
        }
    }

    private fun processIntents() = viewModelScope.launch {
        intents.collect { intent ->
            when (intent) {
                is MoviesSearchListContract.Intent.SearchMovies -> {
                    currentPage = 1
                    isLoadingNextPage = false
                    _state.update { it.copy(isLoading = true, error = null) }
                    searchQuery.value = intent.query
                }

                is MoviesSearchListContract.Intent.OpenDetails ->
                    _effects.emit(NavigateToDetails(intent.id))

                is MoviesSearchListContract.Intent.LoadNextPage -> loadNextPage()
            }
        }
    }
}