package com.example.m_commerce.core.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder

@Composable
fun SvgImage(
    resId: Int,
    contentDescription: String? = null,
    colorFilter: ColorFilter? = null,
    modifier: Modifier = Modifier,
    size: Int = 24
) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(resId)
            .build(),
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier.size(size.dp),
        colorFilter = colorFilter
    )
}

@Composable
fun SvgButton(
    resId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    colorFilter: ColorFilter? = null,
    size: Int = 24,
    onClick: () -> Unit
) {
    SvgImage(
        resId = resId,
        contentDescription = contentDescription,
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp)
            .size(size.dp),
        colorFilter = colorFilter,
    )
}
