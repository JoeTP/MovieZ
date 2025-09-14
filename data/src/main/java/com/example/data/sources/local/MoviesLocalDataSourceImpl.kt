package com.example.data.sources.local

import com.example.core.database.MoviesDao
import com.example.core.database.entities.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesLocalDataSourceImpl @Inject constructor(private val dao: MoviesDao) :
    MoviesLocalDataSource {

    override suspend fun retrievePopularMovies(): Flow<List<MovieEntity>> = dao.getMovies()
    override suspend fun cacheAndUpdateMovies(movies: List<MovieEntity>) = dao.upsertAll(movies)

}