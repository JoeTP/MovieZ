package com.example.core.utils.common_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.fallback
import coil3.request.placeholder
import com.example.core.R

@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    url: String?,
    contentDescription: String = "",
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .placeholder(R.drawable.movie_placeholder)
            .fallback(R.drawable.no_image)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale
    )
}