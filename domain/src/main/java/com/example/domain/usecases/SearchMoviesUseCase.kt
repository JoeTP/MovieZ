package com.example.domain.usecases

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(private val repository: MoviesRepository) :
    UseCase<SearchMoviesUseCaseParams, Flow<ResultState<List<Movie>>>> {
    override suspend fun invoke(params: SearchMoviesUseCaseParams): Flow<ResultState<List<Movie>>> {
        return repository.search(params.query, params.page)
    }
}

data class SearchMoviesUseCaseParams(val query: String, val page: Int)