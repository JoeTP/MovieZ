package com.example.data.repository

import com.example.core.database.entities.MovieEntity
import com.example.core.network.dto.MovieDto
import com.example.core.result_states.ResultState
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.sources.local.MoviesLocalDataSource
import com.example.data.sources.remote.MoviesRemoteDataSource
import com.example.domain.model.Movie
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.Dispatcher
import java.io.IOException
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val localDataSource: MoviesLocalDataSource
) : MoviesRepository {
    override fun getMovies(): Flow<ResultState<List<Movie>>> = flow {
        //TODO: try to get data from localDataSource, if not available => fetch from remoteDataSource
        //TODO: if remoteDataSource is successful => save data to localDataSource
        //TODO: return data from localDataSource
        //TODO: if remoteDataSource is not successful => return from localDataSource and show error message
        //TODO: if localDataSource is not successful => return error message
//
//        emit(ResultState.Loading)
//
//        try {
//            val response = remoteDataSource.fetchPopularMovies()
//            if (response.isSuccessful) {
//                val body = response.body()
//                if (body != null) {
//                    val entities = body.map(MovieDto::toEntity)
//                    localDataSource.cacheAndUpdateMovies(entities)
//                    emit(ResultState.Success(entities.map(MovieEntity::toDomain)))
//                } else {
//                    emit(ResultState.Error("Empty response body"))
//                }
//            } else {
//                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
//                emit(ResultState.Error("HTTP ${response.code()}: $errorMsg"))
//            }
//        } catch (io: IOException) {
//            emit(ResultState.Error("Network error: ${io.message}", io))
//        } catch (t: Throwable) {
//            emit(ResultState.Error("Unexpected error: ${t.message}", t))
//        }
        emit(ResultState.Loading)

        localDataSource.retrievePopularMovies()
            .map { entities -> entities.map(MovieEntity::toDomain) }
            .onEach { if (it.isNotEmpty()) emit(ResultState.Success(it)) }
            .launchIn(CoroutineScope(Dispatchers.IO))

        try {
            val response = remoteDataSource.fetchPopularMovies()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val entities = body.map(MovieDto::toEntity)
                    localDataSource.cacheAndUpdateMovies(entities)
                    // after caching, localDataSource.retrievePopularMovies() will emit again
                } else {
                    emit(ResultState.Error("Empty response from server"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown server error"
                emit(ResultState.Error("HTTP ${response.code()}: $errorMsg"))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network error: ${e.message}", e))
        } catch (t: Throwable) {
            emit(ResultState.Error("Unexpected error: ${t.message}", t))
        }
    }

    override fun search(query: String): Flow<ResultState<List<Movie>>> = flow {

    }

//    override suspend fun getMovieById(id: Int): ResultState<Movie>  {
//
//    }
}