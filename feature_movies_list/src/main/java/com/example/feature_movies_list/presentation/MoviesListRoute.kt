package com.example.feature_movies_list.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.utils.common_components.movie.MovieCard
import com.example.core.utils.common_components.views.EmptyView
import com.example.core.utils.common_components.views.ErrorView
import com.example.core.utils.common_components.views.LoadingView
import com.example.domain.model.Movie


@Composable
fun MoviesListRoute(
    vm: MoviesListViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
) {

    val state by vm.state.collectAsStateWithLifecycle()
    val ctx = LocalContext.current

    LaunchedEffect(Unit) {
        vm.sendIntent(MovieListContract.Intent.Load)
    }

    LaunchedEffect(Unit) {
        vm.effects.collect { effect ->
            when (effect) {
                is MovieListContract.Effect.ShowMessage ->
                    Toast.makeText(ctx, effect.msg, Toast.LENGTH_SHORT).show()

                is MovieListContract.Effect.NavigateToDetails ->
                    onMovieClick(effect.id)

                is MovieListContract.Effect.OpenSearch ->
                    onSearchClick()
            }
        }
    }

    MoviesListScreen(
        state = state,
        onRetry = { vm.sendIntent(MovieListContract.Intent.Retry) },
        onMovieClick = { id -> onMovieClick(id) },
        onSearchClick = onSearchClick,
        onLoadNextPage = { vm.sendIntent(MovieListContract.Intent.LoadNextPage) },
        currentPage = vm.currentPage
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesListScreen(
    state: MovieListContract.State,
    onRetry: () -> Unit,
    onLoadNextPage: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    currentPage: Int,
) {

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Popular Movies $currentPage") },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "search for movies",
                    )
                }
            })
    }) {
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when {
                state.isLoading && state.movies.isEmpty() ->
                    LoadingView()

                state.error != null && state.movies.isEmpty() ->
                    ErrorView(state.error, onRetry)

                state.isEmpty ->
                    EmptyView()

                else ->
                    SuccessState(
                        items = state.movies,
                        onItemClick = onMovieClick,
                        onLoadNextPage = onLoadNextPage,
                        isLoadingNextPage = state.isLoadingNextPage
                    )
            }
        }
    }
}

@Composable
fun SuccessState(
    items: List<Movie>,
    onItemClick: (Int) -> Unit,
    onLoadNextPage: () -> Unit,
    isLoadingNextPage: Boolean,
) {

    MovieList(
        items = items,
        onItemClick = onItemClick,
        isLoadingNextPage = isLoadingNextPage,
        onLoadNextPage = onLoadNextPage
    )
}



@Composable
fun MovieList(
    items: List<Movie>,
    onItemClick: (Int) -> Unit,
    isLoadingNextPage: Boolean,
    onLoadNextPage: () -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        itemsIndexed(items) { index, movie ->
            MovieCard(
                title = movie.title,
                posterUrl = movie.posterUrl,
                releaseYear = movie.releaseYear,
                onClick = { onItemClick(movie.id) }
            )

            if (index == items.lastIndex && !isLoadingNextPage) {
                LaunchedEffect(items.size) {
                    onLoadNextPage()
                }
            }
        }
        if (isLoadingNextPage) {
            item {
                LoadingView()
            }
        }
    }
}