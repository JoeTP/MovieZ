package com.example.domain.usecases

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MoviesRepository) : UseCase<Int, Flow<ResultState<List<Movie>>>> {
    override suspend fun invoke(page: Int): Flow<ResultState<List<Movie>>> {
        return repository.getMovies(page)
    }
}