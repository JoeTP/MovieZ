package com.example.moviez.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.feature_movies_list.presentation.MoviesListRoute

@Composable
fun AppNavSetup(modifier: Modifier , navController: NavHostController, snackbarHostState: SnackbarHostState) {

    val startDestination = AppRouts.MoviesList

    NavHost(navController = navController, startDestination = startDestination) {

        composable<AppRouts.MoviesList> {
            MoviesListRoute(onMovieClick = {}, onSearchClick = {})
        }

//        composable<AppRouts.MovieListSearch> {
//
//        }
//
//        composable<AppRouts.MovieDetails> {
//
//        }

    }
}
