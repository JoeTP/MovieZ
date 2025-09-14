package com.example.feature_search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.result_states.ResultState
import com.example.core.result_states.userMessage
import com.example.domain.usecases.SearchMoviesUseCase
import com.example.domain.usecases.SearchMoviesUseCaseParams
import com.example.feature_search.presentation.MoviesSearchListContract.Effect.NavigateToDetails
import com.example.feature_search.presentation.MoviesSearchListContract.Effect.ShowMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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

    /**
     * The maximum number of pages "Free limit from API".
     */
    private var maxPage = 500

    private var lastSnackMsg: String? = null

    private val intents = MutableSharedFlow<MoviesSearchListContract.Intent>()

    init {
        processIntents()
        observeSearch()
    }

    /**
     * Sends a UI intent to the ViewModel to be handled by [processIntents].
     * @param intent An instance of [MoviesSearchListContract.Intent] representing a user action.
     */
    fun sendIntent(intent: MoviesSearchListContract.Intent) = viewModelScope.launch { intents.emit(intent) }

    /**
     * Observes the search query stream and executes the search use case.
     *
     * - Debounces user input to avoid spamming the API.
     * - Uses [flatMapLatest] to cancel in-flight searches when the query changes.
     * - Updates [state] with loading, success, and error states.
     */
    private fun observeSearch() = viewModelScope.launch {
        searchQuery
            .debounce(700L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
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
                val msg = e.userMessage()
                _effects.emit(ShowMessage(msg))
                _state.update { it.copy(isLoading = false, error = msg) }
            }.collect()
    }

    /**
     * Loads the next page of search results if possible.
     *
     * - Guarded by [isLoadingNextPage], [currentPage] vs [maxPage], and non-blank query.
     * - Appends new results to the existing list and updates pagination state.
     * - Emits a message effect when the free limit is reached or on errors.
     */
    private fun loadNextPage() {
        if (isLoadingNextPage || currentPage >= maxPage || searchQuery.value.isBlank()) return
        isLoadingNextPage = true
        _state.update { it.copy(isLoadingNextPage = true) }
        viewModelScope.launch {
            val params = SearchMoviesUseCaseParams(searchQuery.value, currentPage + 1)
            searchMoviesUseCase(params).onEach { result ->
                when (result) {
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
                        val msg = result.message
                        if (msg != lastSnackMsg) {
                            lastSnackMsg = msg
                            _effects.emit(ShowMessage(msg))
                        }
                        isLoadingNextPage = false
                    }
                }
            }.catch { e ->
                val msg = e.userMessage()
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

    /**
     * Collects intents emitted by the UI and routes them to the appropriate handler.
     *
     * Supported intents:
     * - [MoviesSearchListContract.Intent.SearchMovies]: Starts a new search flow.
     * - [MoviesSearchListContract.Intent.OpenDetails]: Emits a navigation effect to details.
     * - [MoviesSearchListContract.Intent.LoadNextPage]: Triggers pagination.
     */
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