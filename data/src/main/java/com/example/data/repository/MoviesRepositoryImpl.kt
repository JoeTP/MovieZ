package com.example.data.repository

import com.example.core.result_states.ResultState
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.sources.local.MoviesLocalDataSource
import com.example.data.sources.remote.MoviesRemoteDataSource
import com.example.domain.model.MovieDetails
import com.example.domain.model.MoviesPage
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val localDataSource: MoviesLocalDataSource,
) : MoviesRepository {

    override fun getMovies(page: Int): Flow<ResultState<MoviesPage>> = flow {
        emit(ResultState.Loading)
        val localMoviesFlow = localDataSource.retrievePopularMovies()
            .map { entities -> entities.map { it.toDomain() } }
        val cachedMovies = localMoviesFlow.firstOrNull() ?: emptyList()

        if (cachedMovies.isNotEmpty() && page == 1) {
            emit(ResultState.Success(MoviesPage(cachedMovies, 1, page)))
        }
        try {
            val response = remoteDataSource.fetchPopularMovies(page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val movieEntity = body.results.map { it.toEntity() }
                    if (page == 1) localDataSource.cacheAndUpdateMovies(movieEntity)
                    val movieDomain = movieEntity.map { it.toDomain() }
                    emit(ResultState.Success(MoviesPage(movieDomain, body.totalPages, page)))
                } else {
                    if (cachedMovies.isEmpty()) {
                        emit(ResultState.Error("Empty response"))
                    }
                }
            } else {
                if (cachedMovies.isEmpty()) {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown server error"
                    emit(ResultState.Error("HTTP ${response.code()}: $errorMsg"))
                }
            }
        } catch (e: IOException) {
            if (cachedMovies.isEmpty()) {
                emit(ResultState.Error("Network error: ${e.message}", e))
            }
        } catch (t: Throwable) {
            if (cachedMovies.isEmpty()) {
                emit(ResultState.Error("Unexpected error: ${t.message}", t))
            }
        }
    }

    override fun search(query: String, page: Int): Flow<ResultState<MoviesPage>> = flow {
        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.search(query, page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val movieDomain = body.results.map { it.toDomain() }
                    emit(ResultState.Success(MoviesPage(movieDomain, body.totalPages, page)))
                } else {
                    emit(ResultState.Error("Empty response"))
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

    override suspend fun getMovieById(id: Int): Flow<ResultState<MovieDetails>> = flow {
        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.fetchMovie(id)
            if(response.isSuccessful){
                val body = response.body()
                if(body != null){
                    emit(ResultState.Success(body.toDomain()))
                }else{
                    emit(ResultState.Error("Empty response"))
                }
            }else{
                val errorMsg = response.errorBody()?.string() ?: "Unknown server error"
                emit(ResultState.Error("HTTP ${response.code()}: $errorMsg"))
            }
        }catch (e: IOException){
            emit(ResultState.Error("Network error: ${e.message}", e))
        }
    }
}