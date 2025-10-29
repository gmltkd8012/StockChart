package com.leecoder.stockchart.design_system.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseSymbolItem(
    name: String,
    code: String,
    onClick: (Pair<String, String>) -> Unit,
) {
    Row(
       modifier = Modifier
           .fillMaxWidth()
           .wrapContentHeight()
           .padding(horizontal = 8.dp)
           .clickable { onClick(code to name) },
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = name,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}