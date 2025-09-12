package com.example.moviez.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core.utils.common_components.movie_details.MovieDetailsRoute
import com.example.feature_movies_list.presentation.MoviesListRoute
import com.example.feature_search.presentation.MoviesSearchRoute

@Composable
fun AppNavSetup(
    modifier: Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {

    val startDestination = AppRouts.MoviesList

    NavHost(navController = navController, startDestination = startDestination) {

        composable<AppRouts.MoviesList> {
            MoviesListRoute(onMovieClick = {
                navController.navigate(AppRouts.MovieDetails)
            }, onSearchClick = {
                navController.navigate(AppRouts.MovieListSearch)
            })
        }

        composable<AppRouts.MovieListSearch> {
            MoviesSearchRoute(navController){
                navController.navigate(AppRouts.MovieDetails)
            }
        }

        composable<AppRouts.MovieDetails> {
            MovieDetailsRoute(navController)
        }

    }
}




