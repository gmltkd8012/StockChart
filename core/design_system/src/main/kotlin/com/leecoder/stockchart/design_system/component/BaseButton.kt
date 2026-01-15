package com.leecoder.stockchart.design_system.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    backgroundColor: Color = Color(0xFF2196F3),
    disabledBackgroundColor: Color = Color.LightGray,
    textColor: Color = Color.White,
    disabledTextColor: Color = Color.Gray,
    cornerRadius: Dp = 8.dp,
    height: Dp = 52.dp,
    horizontalPadding: Dp = 20.dp,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .height(height),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = disabledTextColor
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}


@Preview
@Composable
private fun BaseButtonPreview() {
    BaseButton(
        text = "Button",
        onClick = {},
    )
}
