package com.example.appcommerceclone.ui.common

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition()
    val startOffsetX = transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB0B0B0),
                Color(0xFF8D8D8D),
                Color(0xFFB0B0B0)
            ),
            start = Offset(x = startOffsetX.value, y = 0f),
            end = Offset(x = startOffsetX.value + size.width.toFloat(), y = size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

@Composable
fun ShimmerItem(
    isLoading: Boolean,
    placeHolderContent: @Composable () -> Unit,
    contentAfterLoading: @Composable () -> Unit
) {
    if (isLoading) placeHolderContent()
    else contentAfterLoading()
}