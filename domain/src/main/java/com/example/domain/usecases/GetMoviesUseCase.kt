package com.example.domain.usecases

import com.example.core.result_states.ResultState
import com.example.domain.model.MoviesPage
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MoviesRepository) : UseCase<Int, Flow<ResultState<MoviesPage>>> {
    override suspend fun invoke(page: Int): Flow<ResultState<MoviesPage>> {
        return repository.getMovies(page)
    }
}