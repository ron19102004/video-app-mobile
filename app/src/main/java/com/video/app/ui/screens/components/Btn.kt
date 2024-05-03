package com.video.app.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import com.video.app.ui.theme.AppColor

@Composable
fun BtnText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        color = AppColor.primary_text
    ),
    shape: RoundedCornerShape? = null,
    height: Dp? = null,
    enabled: Boolean = true,
) {
    ElevatedButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height ?: CONSTANT.UI.HEIGHT_BUTTON),
        shape = shape ?: RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = AppColor.background_container
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