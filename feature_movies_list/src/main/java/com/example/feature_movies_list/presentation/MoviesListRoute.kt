package com.example.feature_movies_list.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.feature_movies_list.MoviesListViewModel


@Composable
fun MoviesListScreen(vm: MoviesListViewModel = hiltViewModel()) {

    val state by vm.state.collectAsStateWithLifecycle()

    Text(modifier =  Modifier.fillMaxSize(), textAlign = TextAlign.Center, text = "Movies List")
}
