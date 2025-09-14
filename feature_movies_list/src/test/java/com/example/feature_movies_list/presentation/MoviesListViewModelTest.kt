package com.example.feature_movies_list.presentation

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import com.example.domain.model.MoviesPage
import com.example.domain.usecases.GetMoviesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MoviesListViewModelTest {

    private lateinit var useCase: GetMoviesUseCase

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
    fun `loads first page and updates state`() = runTest {
        val page = 1
        val page1 = MoviesPage(
            currentPage = 1,
            totalPages = 10,
            movies = listOf(Movie(1, "Title 1", "null", "2024"))
        )
        coEvery { useCase(1) } returns flow {
            emit(ResultState.Success(page1))
        }

        val vm = MoviesListViewModel(useCase)

        useCase(page).first()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.movies.size)
        assertTrue(state.error == null)
        assertFalse(state.isEmpty)
    }

    @Test
    fun `loads next page and appends movies`() = runTest {
        val page1 = MoviesPage(
            currentPage = 1,
            totalPages = 10,
            movies = listOf(Movie(1, "A", null, "2023"))
        )
        val page2 = MoviesPage(
            currentPage = 2,
            totalPages = 10,
            movies = listOf(Movie(2, "B", null, "2022"))
        )

        coEvery { useCase(1) } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(page1))
        }
        coEvery { useCase(2) } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(page2))
        }

        val vm = MoviesListViewModel(useCase)

        vm.sendIntent(MovieListContract.Intent.LoadNextPage)

        val state = vm.state.value
        assertEquals(2, state.movies.size)
        assertFalse(state.isLoadingNextPage)
    }
}
