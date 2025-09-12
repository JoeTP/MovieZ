package com.example.feature_search.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.core.utils.common_components.AppSearchBar
import com.example.core.utils.common_components.EmptyView
import com.example.core.utils.common_components.ErrorView
import com.example.core.utils.common_components.LoadingView
import com.example.core.utils.common_components.topbar.BackButton
import com.example.domain.model.Movie

@Composable
fun MoviesSearchRoute(
    navController: NavHostController,
    vm: MoviesSearchListViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit,
) {

    val state by vm.state.collectAsStateWithLifecycle()
    val ctx = LocalContext.current

    LaunchedEffect(Unit) {
        vm.effects.collect { effect ->
            when (effect) {
                is MoviesSearchListContract.Effect.ShowMessage ->
                    Toast.makeText(ctx, effect.msg, Toast.LENGTH_SHORT).show()

                is MoviesSearchListContract.Effect.NavigateToDetails ->
                    onMovieClick(effect.id)
            }
        }
    }

    MoviesSearchScreen(
        navController = navController,
        state = state,
        onLoadNextPage = { vm.sendIntent(MoviesSearchListContract.Intent.LoadNextPage) },
        onMovieClick = onMovieClick,
        onQueryChange = { vm.sendIntent(MoviesSearchListContract.Intent.SearchMovies(it)) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesSearchScreen(
    navController: NavHostController,
    state: MoviesSearchListContract.State,
    onQueryChange: (String) -> Unit,
    onLoadNextPage: () -> Unit,
    onMovieClick: (Int) -> Unit,
) {

    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current


    Scaffold(topBar = {
        TopAppBar(title = {
            AppSearchBar(
                searchQuery,
                onQueryChange = {
                    searchQuery = it
                    onQueryChange(it)
                },
                onSearch = {
                    keyboardController?.hide()
                },
            )
        }, navigationIcon = { BackButton(navController) })
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when {
                state.isLoading && state.movies.isEmpty() ->
                    LoadingView()

                state.error != null && state.movies.isEmpty() ->
                    ErrorView(state.error, { })

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

    LazyColumn {
        itemsIndexed(items) { index, movie ->
            ListItem(
                headlineContent = { movie.title?.let { Text(it) } },
                supportingContent = { Text(movie.releaseYear ?: "-") },
                modifier = Modifier.clickable { onItemClick(movie.id) }
            )

            if (index == items.lastIndex && !isLoadingNextPage) {
                LaunchedEffect(Unit) {
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