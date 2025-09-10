package com.example.domain.usecases

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MoviesRepository) : UseCase<Unit, Flow<ResultState<List<Movie>>>> {
    override suspend fun invoke(params: Unit): Flow<ResultState<List<Movie>>> {
        return repository.getMovies()
    }
}