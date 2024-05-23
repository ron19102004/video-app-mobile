package com.video.app.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.video.app.config.CONSTANT
import com.video.app.ui.theme.AppColor

@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    shape: RoundedCornerShape? = null,
    placeholder: String? = null,
    label: String? = null,
    isPassword: Boolean? = false,
    height: Dp? = null,
    isError: Boolean = false,
    errorMessage: String = "Error",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    textStyle: TextStyle = TextStyle(
        color = AppColor.primary_text
    ),
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(height ?: CONSTANT.UI.HEIGHT_INPUT)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = shape ?: RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON),
        placeholder = { Text(text = placeholder ?: "", color = Color.DarkGray) },
        textStyle = textStyle,
        label = {
            if (label != null) {
                Text(
                    text = if (isError) errorMessage else label,
                    color = if (isError) AppColor.error else AppColor.primary_text
                )
            }
        },
        singleLine = singleLine,
        isError = isError,
        visualTransformation = if (isPassword == true) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.DarkGray,
            errorIndicatorColor = AppColor.error,
            errorContainerColor = Color.Transparent,
        )
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