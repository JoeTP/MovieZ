package com.example.feature_movies_list.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.utils.common_components.EmptyView
import com.example.core.utils.common_components.ErrorView
import com.example.core.utils.common_components.LoadingView
import com.example.domain.model.Movie
import com.example.feature_movies_list.MovieListContract
import com.example.feature_movies_list.MoviesListViewModel


@Composable
fun MoviesListRoute(
    vm: MoviesListViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
) {

    val state by vm.state.collectAsStateWithLifecycle()
    val ctx = LocalContext.current

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
        onSearchClick = onSearchClick
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesListScreen(
    state: MovieListContract.State,
    onRetry: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
) {

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Popular Movies") },
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
                        onItemClick = onMovieClick
                    )
            }
        }
    }
}

@Composable
fun SuccessState(
    items: List<Movie>,
    onItemClick: (Int) -> Unit,
) {
    LazyColumn {
        items(items) { movie ->
            ListItem(
                headlineContent = { movie.title?.let { Text(it) } },
                supportingContent = { Text(movie.releaseYear ?: "-") },
                modifier = Modifier.clickable { onItemClick(movie.id) }
            )
        }
    }
}
