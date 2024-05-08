package com.video.app.ui.screens.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.video.app.ui.theme.AppColor

@Composable
fun Heading(
    text: String,
    size: TextUnit,
    color: Color = AppColor.primary_text,
    fontWeight: FontWeight = FontWeight.Bold,
    modifier: Modifier = Modifier,
    maxLines:Int = 4
) {
    Text(
        text = text, style = TextStyle(
            fontWeight = fontWeight,
            fontSize = size,
            color = color
        ),
        modifier = modifier,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}