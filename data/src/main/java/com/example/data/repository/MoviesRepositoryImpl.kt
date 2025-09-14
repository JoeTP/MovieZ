package com.example.data.repository

import com.example.core.network.dto.MovieDto
import com.example.core.result_states.ResultState
import com.example.core.result_states.userMessage
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.sources.local.MoviesLocalDataSource
import com.example.data.sources.remote.MoviesRemoteDataSource
import com.example.domain.model.MovieDetails
import com.example.domain.model.MoviesPage
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * Repository implementation that orchestrates data from remote and local sources.
 *
 * Notes:
 * - All returned flows are confined to IO via [flowOn(Dispatchers.IO)].
 * - Popular movies (page 1) are cached locally and served immediately on next app launch.
 * - A temporary content filter is applied to exclude certain genres (see [uncensoredMoviesFilter]).
 */
class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val localDataSource: MoviesLocalDataSource,
) : MoviesRepository {

    //This wasn't intended but I had to put it as a fast solution to filter uncensored movies
    //==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>
//    private val filter = listOf(
//        mapOf("id" to 28, "name" to "Action"),
//        mapOf("id" to 12, "name" to "Adventure"),
//        mapOf("id" to 16, "name" to "Animation"),
//        mapOf("id" to 35, "name" to "Comedy"),
//        mapOf("id" to 80, "name" to "Crime"),
//        mapOf("id" to 99, "name" to "Documentary"),
////    mapOf("id" to 18, "name" to "Drama"),
//        mapOf("id" to 10751, "name" to "Family"),
//        mapOf("id" to 14, "name" to "Fantasy"),
//        mapOf("id" to 36, "name" to "History"),
//        mapOf("id" to 27, "name" to "Horror"),
//        mapOf("id" to 10402, "name" to "Music"),
//        mapOf("id" to 9648, "name" to "Mystery"),
////    mapOf("id" to 10749, "name" to "Romance"),
//        mapOf("id" to 878, "name" to "Science Fiction"),
//        mapOf("id" to 10770, "name" to "TV Movie"),
//        mapOf("id" to 53, "name" to "Thriller"),
//        mapOf("id" to 10752, "name" to "War"),
//        mapOf("id" to 37, "name" to "Western")
//    )

    /**
     * Filters out movies considered uncensored for the app experience.
     *
     * Rules of filtering:
     * - drama only
     * - romance and drama
     * - romance only
     * - mystery + romance + thriller
     * - empty genreIds
     *
     * @param movies Raw movies from Api response.
     * @return A list that excludes the unwanted combinations.
     */
    private fun uncensoredMoviesFilter(movies: List<MovieDto>): List<MovieDto> {
        return movies.filterNot {
            (it.genreIds.contains(18) && it.genreIds.contains(10749)) || (it.genreIds.contains(10749) &&
                    it.genreIds.size == 1) || (it.genreIds.contains(18) && it.genreIds.size == 1) || (it.genreIds.contains(
                9648
            ) && it.genreIds.contains(10749) && it.genreIds.contains(53) || it.genreIds.isEmpty() || it.genreIds.contains(10749) && it.genreIds.contains(27))
        }
    }
    //==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>==>

    /**
     * Retrieves popular movies for a given page.
     *
     * Behavior:
     * - Emits Loading first.
     * - If page == 1 and local cache has data, emits cached data immediately.
     * - Fetches remote page; on success:
     *   - Applies [uncensoredMoviesFilter].
     *   - Caches page 1 to local storage.
     *   - Emits Success with domain models and total pages.
     * - On error:
     *   - If cache is empty, emits a user-friendly error.
     *
     * Threading:
     * - Upstream work runs on [Dispatchers.IO] via [flowOn].
     *
     * @param page The page number to retrieve.
     * @return A flow of [ResultState] containing [MoviesPage] data.
     */
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
                    val filteredMovies = uncensoredMoviesFilter(body.results)
                    val movieEntity = filteredMovies.map { it.toEntity() }
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
                emit(ResultState.Error(e.userMessage(), e))
        } catch (t: Throwable) {
                emit(ResultState.Error(t.userMessage(), t))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Searches for movies matching the provided [query] at the given [page].
     *
     * Behavior:
     * - Emits Loading, then Success with filtered domain results, or user-friendly Error.
     * - Applies [uncensoredMoviesFilter] to remove undesired content.
     *
     * Threading:
     * - Upstream work runs on [Dispatchers.IO] via [flowOn].
     *
     * @param query The search query.
     * @param page The page number to retrieve.
     * @return A flow of [ResultState] containing [MoviesPage] data.
     */
    override fun search(query: String, page: Int): Flow<ResultState<MoviesPage>> = flow {
        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.search(query, page)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val filteredMovies = uncensoredMoviesFilter(body.results)

                    val movieDomain = filteredMovies.map { it.toDomain() }
                    emit(ResultState.Success(MoviesPage(movieDomain, body.totalPages, page)))
                } else {
                    emit(ResultState.Error("Empty response"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown server error"
                emit(ResultState.Error("HTTP ${response.code()}: $errorMsg"))
            }
        } catch (e: IOException) {
            emit(ResultState.Error(e.userMessage(), e))
        } catch (t: Throwable) {
            emit(ResultState.Error(t.userMessage(), t))
        }

    }.flowOn(Dispatchers.IO)

    /**
     * Fetches movie details for a specific [id].
     *
     * Behavior:
     * - Emits Loading, then Success with domain details, or user-friendly Error.
     *
     * Threading:
     * - Upstream work runs on [Dispatchers.IO] via [flowOn].
     *
     * @param id The movie ID to retrieve.
     * @return A flow of [ResultState] containing [MovieDetails] data.
     */
    override suspend fun getMovieById(id: Int): Flow<ResultState<MovieDetails>> = flow {
        emit(ResultState.Loading)
        try {
            val response = remoteDataSource.fetchMovie(id)
            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    emit(ResultState.Success(body.toDomain()))
                } else {
                    emit(ResultState.Error("Empty response"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown server error"
                emit(ResultState.Error("HTTP ${response.code()}: $errorMsg"))
            }
        } catch (e: IOException) {
            emit(ResultState.Error(e.userMessage(), e))
        }
    }.flowOn(Dispatchers.IO)
}