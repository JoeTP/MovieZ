package com.example.data.sources.local

import com.example.core.database.MoviesDao
import com.example.core.database.entities.MovieEntity
import com.example.core.network.dto.MovieDto
import com.example.khatibalamytask.data.remote.MoviesApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MoviesLocalDataSourceImpl @Inject constructor(private val dao: MoviesDao) :
    MoviesLocalDataSource {

    override suspend fun retrievePopularMovies(): Flow<List<MovieEntity>> = dao.getMovies()
    override suspend fun updateMovies(movies: List<MovieEntity>) = dao.upsertAll(movies)

}