package com.example.domain.usecases

import com.example.core.network.dto.MovieDetailsDto
import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val repository: MoviesRepository) :
    UseCase<Int, Flow<ResultState<MovieDetails>>> {
    override suspend fun invoke(movieId: Int): Flow<ResultState<MovieDetails>> {
        return repository.getMovieById(movieId)
    }
}