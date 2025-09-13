package com.example.core.utils.common_components.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.utils.common_components.NetworkImage


@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    title: String,
    posterUrl: String?,
    releaseYear: String?,
    onClick: () -> Unit,
) {

    val shapeInner = RoundedCornerShape(16.dp)
    val shapeOuter = RoundedCornerShape((16 * 1.3).dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(elevation = 4.dp, spotColor = Color.Gray, shape = shapeOuter, clip = true)
            .background(color = Color.White, shape = shapeOuter)
            .clickable { onClick() }
            .clip(shape = shapeOuter)
    ) {
        Box(contentAlignment = Alignment.BottomStart) {

            NetworkImage(
                Modifier
                    .height(260.dp),
                posterUrl
            )
            Text(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(6.dp, 6.dp, 6.dp, 0.dp))
                    .background(color = Color.Black)
                    .padding(4.dp),
                text = releaseYear ?: "Unknown",
                color = Color.White
            )
        }

        Text(
            modifier = Modifier.padding(8.dp),
            text = title,
            // style = titleStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}