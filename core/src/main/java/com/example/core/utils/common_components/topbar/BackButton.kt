package com.example.core.utils.common_components.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.core.R

@Composable
fun BackButton(navController: NavController, modifier: Modifier = Modifier) {
    IconButton(
        modifier = modifier,
        onClick = { navController.navigateUp() }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            tint = MaterialTheme.colorScheme.onBackground,
            contentDescription = stringResource(R.string.back)
        )
    }
}