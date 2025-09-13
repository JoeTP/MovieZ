package com.example.core.utils.common_components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun SmartSpacer(
    size: Dp,
    modifier: Modifier = Modifier
) {
    val parentLayoutType = remember { mutableStateOf<LayoutType?>(null) }

    Box(modifier = modifier.then(DetectParentModifier { layoutType ->
        parentLayoutType.value = layoutType
    })) {
        when (parentLayoutType.value) {
            LayoutType.COLUMN -> Spacer(modifier = Modifier.size(height = size, width = 0.dp))
            LayoutType.ROW -> Spacer(modifier = Modifier.size(width = size, height = 0.dp))
            LayoutType.BOX -> Spacer(modifier = Modifier.size(size))
            else -> Spacer(modifier = Modifier.size(size))
        }
    }
}

// Modifier to detect parent layout type
private class DetectParentModifier(
    val onLayoutDetected: (LayoutType) -> Unit
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return this
    }
}

enum class LayoutType {
    COLUMN, ROW, BOX
}

