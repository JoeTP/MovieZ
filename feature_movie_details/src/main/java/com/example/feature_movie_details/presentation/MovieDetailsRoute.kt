package com.example.feature_movie_details.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.core.R
import com.example.core.theme.MovieZTheme
import com.example.core.utils.common_components.NetworkImage
import com.example.core.utils.common_components.SmartSpacer
import com.example.core.utils.common_components.movie.Plus21Tag
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


    MovieZTheme(
        darkTheme = true,
    ) {

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
}


@Composable
fun SuccessState(movie: MovieDetails, modifier: Modifier) {

    val radius = 40.dp
    val roundedCornerShape = RoundedCornerShape(topStart = radius, topEnd = radius)

    val languages = movie.spokenLanguages.joinToString(", ") { it.englishName }.split(",").joinToString(", ")
    val countries = movie.originCountry.joinToString(", ")
    val genres = movie.genres.joinToString(", ") { it.name }
    val tagline = movie.tagline?.split(". ")?.joinToString(" / ")
    Log.d("TAG", "SuccessState: ${movie.spokenLanguages} === $languages")

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
            if (movie.adult) Plus21Tag()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {

                Box(modifier = Modifier.height(500.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Image(
                            modifier = Modifier.height(20.dp),
                            painter = painterResource(id = R.drawable.imdb),
                            contentDescription = "imdb"
                        )
                        SmartSpacer(8.dp)
                        Text(movie.voteAverage.toString())
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.8f),
                            shape = roundedCornerShape
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),

                        ) {
                        TouchIndicator(Modifier.align(Alignment.CenterHorizontally))
                        SmartSpacer(12.dp)
                        Text(
                            text = "Genres: $genres",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        )
                        SmartSpacer(8.dp)
                        tagline?.let {
                            Text(
                                text = "Tag line: $it",
                                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
                            )
                        }
                        HorizontalDivider(Modifier.padding(14.dp))
                        Text(movie.overview, textAlign = TextAlign.Justify)
                        HorizontalDivider(Modifier.padding(14.dp))
                        Text("Languages: $languages")
                        Text("Country: $countries")
                        Text(
                            "Released ${movie.releaseYear}",
                            style = TextStyle(fontStyle = FontStyle.Italic)
                        )
                        SmartSpacer(300.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun TouchIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier
            .width(60.dp)
            .height(5.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), shape = RoundedCornerShape(100.dp))
    )
}

@Composable
fun GenreChip(modifier: Modifier = Modifier, genre: String) {
    val radius = 8.dp
    val roundedCornerShape = RoundedCornerShape(radius)
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .background(Color.Red, roundedCornerShape)
                .border(2.dp, Color.White, roundedCornerShape)
                .padding(8.dp),
            text = genre,
            style = TextStyle(
//                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}