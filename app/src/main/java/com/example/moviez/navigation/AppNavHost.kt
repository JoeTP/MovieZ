package com.example.moviez.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.feature_movie_details.presentation.MovieDetailsRoute
import com.example.feature_movies_list.presentation.MoviesListRoute
import com.example.feature_search.presentation.MoviesSearchRoute

@Composable
fun AppNavSetup() {

    val navController = rememberNavController()

    val startDestination = AppRouts.MoviesList

    NavHost(navController = navController, startDestination = startDestination) {

        composable<AppRouts.MoviesList> {
            MoviesListRoute(
                onMovieClick = { id ->
                    navController.navigate(AppRouts.MovieDetails(id))
                },
                onSearchClick = {
                    navController.navigate(AppRouts.MovieListSearch)
                },
            )
        }

        composable<AppRouts.MovieListSearch> {
            MoviesSearchRoute(navController) { id ->
                navController.navigate(AppRouts.MovieDetails(id))
            }
        }

        composable<AppRouts.MovieDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<AppRouts.MovieDetails>()
            MovieDetailsRoute(navController, args.id)
        }

    }
}
