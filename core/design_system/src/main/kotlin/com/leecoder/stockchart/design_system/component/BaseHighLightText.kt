package com.leecoder.stockchart.design_system.component

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.substring
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun BaseHighLightText(
    text: String,
    highLight: String,
    fontSize: Int,
    fontWeight: FontWeight,
    fontColor: Color,
    highLightColor: Color,
) {
    Text(
        text = text.setHighLight(text, highLight, highLightColor),
        style = TextStyle(
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
        ),
        color = fontColor
    )
}

private fun String.setHighLight(
    text: String,
    highLight: String,
    highLightColor: Color,
): AnnotatedString {
    val parseText = text.replace(" ", "").lowercase()
    val parseHighLight = highLight.replace(" ", "").lowercase()

    return try {
        val start = parseText.indexOf(parseHighLight)
        val end = start + parseHighLight.length

        if (start > -1) {
            buildAnnotatedString {
                append(this@setHighLight.substring(0, start))

                withStyle(style = SpanStyle(color = highLightColor)) {
                    append(this@setHighLight.substring(start, end))
                }

                append(this@setHighLight.substring(end, this@setHighLight.length))
            }
        } else {
            AnnotatedString(this)
        }

    } catch (e: Exception) {
        Log.e("lynn", "setHighLight Error -> $e")
        AnnotatedString(this)
    }
}