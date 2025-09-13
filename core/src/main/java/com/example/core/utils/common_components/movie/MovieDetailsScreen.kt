package com.example.core.utils.common_components.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.core.utils.common_components.topbar.DefaultTopBar

@Composable
fun MovieDetailsRoute(navController: NavHostController?) {
    Scaffold(topBar = {
        DefaultTopBar(
            title = "Movie Name",
            navController = navController,
            titleCentered = true,
        )
    }) {
        Column(Modifier.padding(it)
            .fillMaxSize()
            .background(Color.Red)) {

            Text("Movie Details")
        }
    }
}