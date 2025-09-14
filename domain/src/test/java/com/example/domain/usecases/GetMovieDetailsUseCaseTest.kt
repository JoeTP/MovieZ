package com.example.domain.usecases

import com.example.core.result_states.ResultState
import com.example.domain.model.MovieDetails
import com.example.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
class GetMovieDetailsUseCaseTest {

    private val repo: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        useCase = GetMovieDetailsUseCase(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test Success when repository returns details`() = runTest {
        val id = 42
        val details = MovieDetails(
            id = 256,
            title = "Sample",
            overview = "Overview",
            posterUrl = "dummy url",
            releaseYear = "2020",
            voteAverage = 7.5,
            adult = false,
            spokenLanguages = emptyList(),
            originCountry = emptyList(),
            genres = emptyList(),
            tagline = null,
            originalTitle = "quod",
            backdropPath = "litora",
            homepage = "aliquid",
            imdbID = "2.5",
            originalLanguage = "English",
            popularity = 2.3,
            status = "released"
        )
        coEvery { repo.getMovieById(id) } returns flow { emit(ResultState.Success(details)) }

        val collector = useCase(id).first()

        assertTrue(collector is ResultState.Success)
        val state = collector as ResultState.Success
        assertEquals(details, state.data)
        //assertion of calling
        coVerify(exactly = 1) { repo.getMovieById(id) }
    }

    @Test
    fun `emits Error when repository fails`() = runTest {
        val id = 101
        val errorMessage = "Not found"
        coEvery { repo.getMovieById(id) } returns flowOf(ResultState.Error(errorMessage))

        val collector = useCase(id).first()

        assertTrue(collector is ResultState.Error)
        val error = collector as ResultState.Error
        assertEquals(errorMessage, error.message)
        //assertion of calling
        coVerify(exactly = 1) { repo.getMovieById(id) }
    }
}
