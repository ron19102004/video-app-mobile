package com.video.app.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.video.app.config.CONSTANT

@Composable
fun Input(
    modifier: Modifier = Modifier, value: String,
    onValueChange: (String) -> Unit,
    shape: RoundedCornerShape? = null,
    placeholder: String? = null,
    label: String? = null,
    isPassword: Boolean? = false,
    height: Dp? = null,
    isError: Boolean = false,
    errorMessage: String = "Error"
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(height ?: CONSTANT.UI.HEIGHT_INPUT),
        shape = shape ?: RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON),
        placeholder = { Text(text = placeholder ?: "") },
        label = {
            if (label != null) {
                Text(text = if (isError) errorMessage else label)
            }
        },
        singleLine = true,
        isError = isError,
        visualTransformation = if (isPassword == true) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Preview(showBackground = true)
@Composable
fun InputPreview() {
    Input(
        value = "Hello World",
        onValueChange = {},
        placeholder = "dung@gmail.com",
        label = "Email"
    )
}