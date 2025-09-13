package com.example.core.utils.common_components.movie

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.utils.common_components.NetworkImage
import com.example.core.utils.helper_compose.bottomBorder


@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    title: String,
    posterUrl: String?,
    releaseYear: String?,
    onClick: () -> Unit,
) {

    val color = MaterialTheme.colorScheme.surfaceContainer
    val shapeOuter = RoundedCornerShape((16 * 1.3).dp)
    Column(
        modifier = modifier
            .shadow(elevation = 6.dp, spotColor = Color.Green, shape = shapeOuter, clip = false)
            .background(color = color, shape = shapeOuter)
    ) {
        Column(
            Modifier
                .clip(shape = shapeOuter)
                .bottomBorder(2.dp, color = Color.Green)
                .clickable(onClick = onClick)

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
                )
            }

            Text(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                text = title,
                // style = titleStyle,
                maxLines = 2,
                textAlign =  TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}