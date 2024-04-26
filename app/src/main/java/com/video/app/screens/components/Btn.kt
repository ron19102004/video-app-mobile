package com.video.app.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.video.app.config.CONSTANT

@Composable
fun BtnText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle(
        color = MaterialTheme.colorScheme.background,
        fontWeight = FontWeight.SemiBold,
    ),
    shape: RoundedCornerShape? = null,
    height: Dp? = null
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height ?: CONSTANT.UI.HEIGHT_BUTTON),
        shape = shape ?: RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON),
        colors = ButtonDefaults.buttonColors(

        )
    ) {
        Text(text = text, style = textStyle)
    }
}

@Preview(showBackground = true)
@Composable
fun BtnPreview() {
    BtnText(onClick = {}, text = "Login")
}