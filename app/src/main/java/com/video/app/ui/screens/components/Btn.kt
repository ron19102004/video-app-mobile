package com.video.app.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.video.app.R
import com.video.app.config.CONSTANT
import com.video.app.ui.theme.AppColor

@Composable
fun BtnText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    textColor: Color = AppColor.primary_text,
    textSize: TextUnit = CONSTANT.UI.TEXT_SIZE.SM,
    textStyle: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        fontSize = textSize
    ),
    shape: RoundedCornerShape? = null,
    height: Dp? = null,
    enabled: Boolean = true,
    buttonColor: Color = AppColor.background_container
) {
    ElevatedButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier
            .height(height ?: CONSTANT.UI.HEIGHT_BUTTON),
        shape = shape ?: RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = buttonColor
        )
    ) {
        Text(text = text, style = textStyle)
    }
}

@Composable
fun BtnImgText(onClick: () -> Unit, text: String, painter: Painter) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imgModifier = Modifier.size(30.dp)
        Box(modifier = imgModifier) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = imgModifier
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Heading(
            text = text,
            size = CONSTANT.UI.TEXT_SIZE.SM
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BtnPreview() {
    BtnText(onClick = {}, text = "Login")
}