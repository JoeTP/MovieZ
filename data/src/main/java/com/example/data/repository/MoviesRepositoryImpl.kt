package com.example.data.repository

import com.example.core.result_states.ResultState
import com.example.data.sources.local.MoviesLocalDataSource
import com.example.data.sources.remote.MoviesRemoteDataSource
import com.example.domain.model.Movie
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val localDataSource: MoviesLocalDataSource
) : MoviesRepository {
    override fun getMovies(): Flow<ResultState<List<Movie>>> {
        //TODO: try to get data from localDataSource, if not available => fetch from remoteDataSource
        //TODO: if remoteDataSource is successful => save data to localDataSource
        //TODO: return data from localDataSource
        //TODO: if remoteDataSource is not successful => return from localDataSource and show error message
        //TODO: if localDataSource is not successful => return error message
    }

    override fun search(query: String): Flow<ResultState<List<Movie>>> = flow {

    }

    override suspend fun getMovieById(id: Int): ResultState<Movie>  {
    }
}