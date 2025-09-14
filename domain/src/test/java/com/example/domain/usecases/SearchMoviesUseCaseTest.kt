package com.example.domain.usecases

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import com.example.domain.model.MoviesPage
import com.example.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMoviesUseCaseTest {

    private val repo: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: SearchMoviesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        useCase = SearchMoviesUseCase(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits Success for query when repository succeeds`() = runTest {
        val params = SearchMoviesUseCaseParams(query = "batman", page = 1)
        val movie = Movie(
            id = 6481,
            title = "fabellas",
            posterUrl = "https://duckduckgo.com/?q=ponderum",
            releaseYear = "2000"
        )
        val mockMoviesPage = MoviesPage(
            currentPage = 1,
            totalPages = 200,
            movies = listOf(movie)
        )

        coEvery { repo.search(params.query, params.page) } returns flow {
            emit(ResultState.Success(mockMoviesPage))
        }

        val collector = useCase(params).first()

        val success = collector as ResultState.Success
        assertEquals(mockMoviesPage, success.data)
        //assertion of calling
        coVerify(exactly = 1) { repo.search(params.query, params.page) }
    }

    @Test
    fun `emits Error when repository search fails`() = runTest {
        val params = SearchMoviesUseCaseParams(query = "matrix", page = 2)
        val errorMessage = "Server Error"
        coEvery { repo.search(params.query, params.page) } returns flowOf(ResultState.Error(errorMessage))

        val collector = useCase(params).first()

        assertTrue(collector is ResultState.Error)
        val error = collector as ResultState.Error
        assertEquals(errorMessage, error.message)
        //assertion of calling
        coVerify(exactly = 1) { repo.search(params.query, params.page) }
    }

}
