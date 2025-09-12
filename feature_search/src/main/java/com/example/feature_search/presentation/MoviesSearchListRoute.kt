package com.example.feature_search.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.core.utils.common_components.EmptyView
import com.example.core.utils.common_components.ErrorView
import com.example.core.utils.common_components.LoadingView
import com.example.domain.model.Movie

@Composable
fun MoviesSearchRoute(
    navController: NavHostController?,
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
        state = state,
        onLoadNextPage = { vm.sendIntent(MoviesSearchListContract.Intent.LoadNextPage) },
        onMovieClick = onMovieClick,
        onQueryChange = {vm.sendIntent(MoviesSearchListContract.Intent.SearchMovies(it))}
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesSearchScreen(
    state: MoviesSearchListContract.State,
    onQueryChange: (String) -> Unit,
    onLoadNextPage: () -> Unit,
    onMovieClick: (Int) -> Unit,
) {

    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    Scaffold(topBar = {
//        AppSearchBar()
        TopAppBar(title = {
            AppSearchBar(
                searchQuery,
                onQueryChange = {
                    searchQuery = it
                    onQueryChange(it)
                },
                onSearch = {
                    isSearchExpanded = false
                },
//                expanded = isSearchExpanded,
//                onExpandedChange = { isSearchExpanded = it })
            )
        })
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
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search...",
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        placeholder = { Text(text = hint) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search...",
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
) {
    var textFieldWidth by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp)
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldWidth = coordinates.size.width.toFloat()
                    },
                placeholder = { Text(text = hint) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { onQueryChange("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch() }
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                )
            )
        }

        if (!expanded) {
            IconButton(
                onClick = { onExpandedChange(true) },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Open search"
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