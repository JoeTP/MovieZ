package com.example.domain.usecases

import com.example.core.result_states.ResultState
import com.example.domain.model.MoviesPage
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(private val repository: MoviesRepository) :
    UseCase<SearchMoviesUseCaseParams, Flow<ResultState<MoviesPage>>> {
    override suspend fun invoke(params: SearchMoviesUseCaseParams): Flow<ResultState<MoviesPage>> {
        return repository.search(params.query, params.page)
    }
}

data class SearchMoviesUseCaseParams(val query: String, val page: Int)