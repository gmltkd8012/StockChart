package com.leecoder.stockchart.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leecoder.stockchart.model.screen.Screen
import com.leecoder.stockchart.ui.extension.clickableWithoutRipple

@Composable
fun NavButton(
    icon: ImageVector,
    title: String,
    screen: Screen,
    isSelected: Boolean,
    isShownBadge: Boolean,
    onClick: () -> Unit,
) {
    val contentColor: Color = if (isSelected) Color.White else Color.LightGray

    Box(
        modifier = Modifier
            .width(54.dp)
            .height(54.dp)
    ) {
        if (isShownBadge && screen == Screen.Alarm) {
            BaseAlarmBadge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp, end = 4.dp)
            )
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .clickableWithoutRipple (
                    onClick = onClick
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(contentColor),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}