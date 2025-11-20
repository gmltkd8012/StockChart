package com.leecoder.stockchart.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseSymbolItem(
    name: String,
    code: String,
    region: String,
    keyword: String,
    onClick: (Pair<String, String>) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp)
            .clickable { onClick(code to name) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                BaseHighLightText(
                    text = code,
                    highLight = keyword,
                    fontSize = 20,
                    fontWeight = FontWeight.Bold,
                    fontColor = Color.Black,
                    highLightColor = Color.Blue
                )

                Spacer(Modifier.height(12.dp))

                BaseHighLightText(
                    text = name,
                    highLight = keyword,
                    fontSize = 17,
                    fontWeight = FontWeight.Normal,
                    fontColor = Color.DarkGray,
                    highLightColor = Color.Blue
                )
            }

            Text(
                text = region,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color =  Color.LightGray
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(color = Color.LightGray)
        )
    }

}



@Preview(showBackground = true)
@Composable
fun BaseSymbolItemPreview() {
    BaseSymbolItem(
        name = "종목",
        code = "코드",
        region = "지역",
        keyword = "코",
        onClick = {}
    )
}