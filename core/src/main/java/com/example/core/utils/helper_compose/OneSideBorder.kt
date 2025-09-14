package com.example.core.utils.helper_compose



import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp

@SuppressLint("SuspiciousModifierThen")
fun Modifier.leftBorder(strokeWidth: Dp, color: Color): Modifier = then(
    drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                this.color = color
                this.strokeWidth = strokeWidthPx
            }
            canvas.drawLine(
                Offset(0f, 0f),
                Offset(0f, size.height),
                paint
            )
        }
    }
)

@SuppressLint("SuspiciousModifierThen")
fun Modifier.rightBorder(strokeWidth: Dp, color: Color): Modifier = then(
    drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                this.color = color
                this.strokeWidth = strokeWidthPx
            }
            canvas.drawLine(
                Offset(size.width, 0f),
                Offset(size.width, size.height),
                paint
            )
        }
    }
)

@SuppressLint("SuspiciousModifierThen")
fun Modifier.topBorder(strokeWidth: Dp, color: Color): Modifier = then(
    drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                this.color = color
                this.strokeWidth = strokeWidthPx
            }
            canvas.drawLine(
                Offset(0f, 0f),
                Offset(size.width, 0f),
                paint
            )
        }
    }
)

@SuppressLint("SuspiciousModifierThen")
fun Modifier.bottomBorder(strokeWidth: Dp, color: Color): Modifier = then(
    drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                this.color = color
                this.strokeWidth = strokeWidthPx
            }
            canvas.drawLine(
                Offset(0f, size.height),
                Offset(size.width, size.height),
                paint
            )
        }
    }
)