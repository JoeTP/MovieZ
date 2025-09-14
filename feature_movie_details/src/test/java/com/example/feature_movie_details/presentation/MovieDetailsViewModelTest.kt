package com.example.feature_movie_details.presentation

import com.example.core.result_states.ResultState
import com.example.domain.model.MovieDetails
import com.example.domain.usecases.GetMovieDetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private lateinit var useCase: GetMovieDetailsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        useCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load details updates state to success`() = runTest {
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
        coEvery { useCase(id) } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(details))
        }

        val vm = MovieDetailsViewModel(useCase)
        vm.sendIntent(MovieDetailsContract.Intent.Load(id))

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(details, state.movieDetails)
    }

    @Test
    fun `load error sets error in state`() = runTest {
        val id = 40222222
        val errorMsg = "Not found"
        coEvery { useCase(id) } returns flowOf(ResultState.Error(errorMsg))

        val vm = MovieDetailsViewModel(useCase)
        vm.sendIntent(MovieDetailsContract.Intent.Load(id))

        val state = vm.state.value
        assertEquals(errorMsg, state.error)
    }
}
