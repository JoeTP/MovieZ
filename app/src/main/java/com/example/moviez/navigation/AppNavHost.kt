package com.example.moviez.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.feature_movies_list.presentation.MoviesListScreen

@Composable
fun AppNavSetup(modifier: Modifier , navController: NavHostController, snackbarHostState: SnackbarHostState) {

    val startDestination = AppRouts.MoviesList

    NavHost(navController = navController, startDestination = startDestination) {

        composable<AppRouts.MoviesList> {
            MoviesListScreen()
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
