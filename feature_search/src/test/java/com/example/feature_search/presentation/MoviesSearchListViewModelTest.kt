package com.example.feature_search.presentation

import com.example.core.result_states.ResultState
import com.example.domain.model.Movie
import com.example.domain.model.MoviesPage
import com.example.domain.usecases.SearchMoviesUseCase
import com.example.domain.usecases.SearchMoviesUseCaseParams
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
class MoviesSearchListViewModelTest {

    private lateinit var useCase: SearchMoviesUseCase

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
    fun `search intent loads first page and updates state`() = runTest {
        val params = SearchMoviesUseCaseParams(query = "batman", page = 1)
        val page1 = MoviesPage(
            currentPage = 1,
            totalPages = 5,
            movies = listOf(Movie(1, "Batman", null, "2005"))
        )
        coEvery { useCase(params) } returns flow {
            emit(ResultState.Success(page1))
        }

        val vm = MoviesSearchListViewModel(useCase)
        vm.sendIntent(MoviesSearchListContract.Intent.SearchMovies("batman"))

        // passing debounce time (700ms)
        advanceTimeBy(800)

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.movies.size)
        assertTrue(state.error == null)
        assertFalse(state.isEmpty)
    }

    @Test
    fun `load next page and appends movies`() = runTest {
        val p1 = MoviesPage(
            currentPage = 1,
            totalPages = 5,
            movies = listOf(Movie(1, "A", null, "2020"))
        )
        val p2 = MoviesPage(
            currentPage = 2,
            totalPages = 5,
            movies = listOf(Movie(2, "B", null, "2021"))
        )

        coEvery { useCase(SearchMoviesUseCaseParams("batman", 1)) } returns flow {
            emit(ResultState.Success(p1))
        }
        coEvery { useCase(SearchMoviesUseCaseParams("batman", 2)) } returns flow {
            emit(ResultState.Success(p2))
        }

        val vm = MoviesSearchListViewModel(useCase)
        vm.sendIntent(MoviesSearchListContract.Intent.SearchMovies("batman"))
        advanceTimeBy(800)

        vm.sendIntent(MoviesSearchListContract.Intent.LoadNextPage)

        val state = vm.state.value
        assertEquals(2, state.movies.size)
        assertTrue(state.movies.any { it.id == 1 })
        assertTrue(state.movies.any { it.id == 2 })
        assertFalse(state.isLoadingNextPage)
    }

    @Test
    fun `error during search sets error in state`() = runTest {
        val query = "x"
        val params = SearchMoviesUseCaseParams(query = query, page = 1)
        val errorMsg = "Not Found"
        coEvery { useCase(params) } returns flowOf(ResultState.Error(errorMsg))

        val vm = MoviesSearchListViewModel(useCase)
        vm.sendIntent(MoviesSearchListContract.Intent.SearchMovies(query))
        advanceTimeBy(800)

        val state = vm.state.value
        assertEquals(errorMsg, state.error)
        assertFalse(state.isLoading)
    }
}
