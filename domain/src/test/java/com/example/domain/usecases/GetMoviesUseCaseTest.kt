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
class GetMoviesUseCaseTest {

    private val repo: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetMoviesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        useCase = GetMoviesUseCase(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits Success when repository succeeds`() = runTest {
        val movie = Movie(
            id = 2244,
            title = "Delicata",
            posterUrl = "https://example.com/tristique.jpg",
            releaseYear = "1998"
        )
        val mockMoviePage = MoviesPage(
            currentPage = 1,
            totalPages = 20,
            movies = listOf(movie)
        )


        coEvery { repo.getMovies(1) } returns flow {
            emit(ResultState.Success(mockMoviePage))
        }

        val collector = useCase(1).first()

        val success = collector as ResultState.Success
        assertEquals(mockMoviePage, success.data)
        //assertion of calling
        coVerify(exactly = 1) { repo.getMovies(1) }
    }

    @Test
    fun `emits Error when repository fails`() = runTest {
        val errorMessage = "Network error"
        coEvery { repo.getMovies(2) } returns flowOf(ResultState.Error(errorMessage))

        val collector = useCase(2).toList()

        assertTrue(collector.first() is ResultState.Error)
        val error = collector.first() as ResultState.Error
        assertEquals(errorMessage, error.message)
        //assertion of calling
        coVerify(exactly = 1) { repo.getMovies(2) }
    }

}



