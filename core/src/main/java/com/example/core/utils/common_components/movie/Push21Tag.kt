package com.example.core.utils.common_components.movie

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.core.R

@Composable
fun Plus21Tag(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.plus21),
        contentDescription = stringResource(R.string.plus_21)
    )
}