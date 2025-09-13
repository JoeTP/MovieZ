package com.example.feature_movie_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.core.utils.common_components.NetworkImage
import com.example.core.utils.common_components.SmartSpacerV
import com.example.core.utils.common_components.topbar.DefaultTopBar
import com.example.core.utils.common_components.views.ErrorView
import com.example.core.utils.common_components.views.LoadingView
import com.example.domain.model.MovieDetails

@Composable
fun MovieDetailsRoute(
    navController: NavHostController,
    id: Int,
    vm: MovieDetailsViewModel = hiltViewModel(),
) {

    val state = vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.sendIntent(MovieDetailsContract.Intent.Load(id))
    }


    MovieDetailsScreen(state = state.value, navController = navController)
}

@Composable
fun MovieDetailsScreen(state: MovieDetailsContract.State, navController: NavHostController) {


    Scaffold(topBar = {
        DefaultTopBar(
            title = state.movieDetails?.title ?: "",
            navController = navController,
            titleCentered = true,
        )
    }) { paddingValues ->

        when {
            state.isLoading -> LoadingView()
            state.error != null && state.movieDetails == null -> ErrorView(state.error, {})
            state.movieDetails != null -> SuccessState(
                movie = state.movieDetails,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

//@Composable
//fun SuccessState(movie: MovieDetails, modifier: Modifier) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//    ) {
//        NetworkImage(
//            modifier = Modifier.fillMaxWidth(),
//            url = movie.posterUrl,
//            contentDescription = "movie poster",
//            contentScale = ContentScale.Fit
//        )
//        Surface(Modifier.fillMaxWidth()) {
//
//        }
//    }
//}

@Composable
fun SuccessState(movie: MovieDetails, modifier: Modifier) {

    val radius = 40.dp
    val roundedCornerShape = RoundedCornerShape(topStart = radius, topEnd = radius)

    Box(modifier = modifier.fillMaxSize()) {
        // Background image
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NetworkImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),
                url = movie.posterUrl,
                contentDescription = "movie poster",
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.height(500.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                    .height(500.dp)

                        .background(
                            //TODO: Remove hardcoded color
                            Color.Black.copy(alpha = 0.7f),
                            shape = roundedCornerShape
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        TouchIndicator()
                        SmartSpacerV(12.dp)

                        Text(movie.overview, textAlign = TextAlign.Justify)


                    }
                }
            }
        }
    }
}

@Composable
fun TouchIndicator() {
    Box(
        Modifier
            .width(60.dp)
            .height(6.dp)
            .background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(100.dp))
    )
}