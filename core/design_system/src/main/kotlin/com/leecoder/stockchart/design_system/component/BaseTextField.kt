package com.leecoder.stockchart.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    hint: String,
    maxLength: Int = 20,
    onTextChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            value = textFieldValue,
            onValueChange = { text ->
                if (text.length <= maxLength) onTextChanged(text)
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Black
            ),
            singleLine = true,
            cursorBrush = SolidColor(Color.Black),
            decorationBox =
                @Composable { innerTextField ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextFieldDefaults.DecorationBox(
                            value = textFieldValue,
                            innerTextField = innerTextField,
                            placeholder = {
                                Text(
                                    text = hint,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray,
                                )
                            },
                            enabled = true,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = remember { MutableInteractionSource() },
                            container = {},
                            contentPadding = PaddingValues(0.dp)
                        )

                        if (textFieldValue.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color.Gray),
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.clickable {
                                        onTextChanged("")
                                    }
                                )
                            }
                        }
                    }
                }
        )
    }
}
