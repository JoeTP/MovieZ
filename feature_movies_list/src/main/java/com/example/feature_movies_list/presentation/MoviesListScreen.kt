package com.example.feature_movies_list.presentation

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.feature_movies_list.MoviesListViewModel


@Composable
fun MoviesListScreen(vm: MoviesListViewModel = hiltViewModel()) {

    val state = vm.state.collectAsState()

    Text("Movies List")
}
