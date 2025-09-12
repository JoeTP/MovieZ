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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesSearchListViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesSearchListContract.State())
    val state: StateFlow<MoviesSearchListContract.State> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<MoviesSearchListContract.Effect>()
    val effects: SharedFlow<MoviesSearchListContract.Effect> = _effects.asSharedFlow()

    private val intents = MutableSharedFlow<MoviesSearchListContract.Intent>()

    private var currentPage = 1
    private var isLoadingNextPage = false
    private val maxPage = 500
//    private var currentQuery: String = ""
    private val _query = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _query.asStateFlow()



    init {
        processIntents()
    }


    fun sendIntent(intent: MoviesSearchListContract.Intent) =
        viewModelScope.launch { intents.emit(intent) }


    private fun searchMovies(query: String) {



        viewModelScope.launch {
            currentQuery
                .debounce(700)
                .collect { q ->

                }

            val searchMoviesUseCaseParams = SearchMoviesUseCaseParams(query, currentPage)
            searchMoviesUseCase(searchMoviesUseCaseParams).onEach { result ->
                _state.update {
                    it.reduce(result)
//                val newMovies = if (result is ResultState.Success) result.data else emptyList()
//                it.copy(
//                    movies = newMovies,
//                    isLoading = false,
//                    error = null,
//                    isLoadingNextPage = false
//                )

                }
            }.catch { e ->
                val msg = e.message ?: "Unexpected error"
                _state.update { it.copy(isLoading = false, error = msg) }
                _effects.emit(ShowMessage(msg))
            }.collect()
        }
    }


    private fun loadNextPage() {
        if (isLoadingNextPage || currentPage >= maxPage || currentQuery.isBlank()) return
        isLoadingNextPage = true
        _state.update { it.copy(isLoadingNextPage = true) }
        viewModelScope.launch {
            val searchMoviesUseCaseParams = SearchMoviesUseCaseParams(currentQuery, currentPage + 1)
            searchMoviesUseCase(searchMoviesUseCaseParams).onEach { result ->
                _state.update {
                    val newMovies = if (result is ResultState.Success) result.data else emptyList()
                    val allMovies = it.movies + newMovies
                    it.copy(
                        movies = allMovies,
                        isLoading = false,
                        error = null,
                        isLoadingNextPage = false
                    )
                }
                currentPage++
                isLoadingNextPage = false
                if (currentPage >= maxPage) _effects.emit(ShowMessage(
                        "Free Limit Reached"
                    )
                )
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
        intents.onEach { intent ->
            when (intent) {
                is MoviesSearchListContract.Intent.SearchMovies -> {
                    currentPage = 1
                    isLoadingNextPage = false
                    currentQuery = intent.query
                    _state.update { it.copy(isLoading = true, error = null) }
                    searchMovies(currentQuery)
                }

                is MoviesSearchListContract.Intent.OpenDetails -> _effects.emit(
                    NavigateToDetails(intent.id)
                )

                is MoviesSearchListContract.Intent.LoadNextPage -> loadNextPage()
            }
        }.collect()
    }
}