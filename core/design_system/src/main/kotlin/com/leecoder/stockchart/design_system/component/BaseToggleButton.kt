package com.leecoder.stockchart.design_system.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.leecoder.stockchart.ui.extension.clickableWithoutRipple
import kotlin.math.roundToInt

@Composable
fun BaseToggleButton(
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    width: Dp = 50.dp,
    height: Dp = 28.dp,
) {
    val density = LocalDensity.current
    val minBound = with(density) { 0.dp.toPx() }
    val maxBound = with(density) { width.toPx() - 28.dp.toPx() }

    val state by animateFloatAsState(
        targetValue = if (isChecked) maxBound else minBound,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = ""
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(
                color = if (isChecked) Color.Green else Color.LightGray,
                shape = RoundedCornerShape(size = 15.dp),
            )
            .padding(2.dp)
            .clickableWithoutRipple {
                onCheckedChanged(!isChecked)
            },
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(state.roundToInt(), 0) }
                .size(24.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )

        )
    }
}

@Preview
@Composable
fun BaseToggleButtonPreview() {
    BaseToggleButton(
        isChecked = true,
        onCheckedChanged = {}
    )
}