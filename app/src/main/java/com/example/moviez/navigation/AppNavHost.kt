package com.example.moviez.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

@Composable
fun AppNavSetup(navController: NavHostController, snackbarHostState: SnackbarHostState) {

    val startDestination = AppRouts.MoviesList

    NavHost(navController = navController, startDestination = startDestination) {

        composable<AppRouts.MoviesList> {
            
        }

        composable<AppRouts.MovieListSearch> {

        }

        composable<AppRouts.MovieDetails> {

        }

    }
}
