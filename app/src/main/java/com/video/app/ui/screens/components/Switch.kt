package com.video.app.ui.screens.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.video.app.ui.theme.AppColor

@Composable
fun SwitchComponent(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedTrackColor = AppColor.primary_content,
            checkedThumbColor = Color.White,
            uncheckedTrackColor = AppColor.primary_text,
            uncheckedThumbColor = AppColor.background_container
        ),
        modifier = modifier
    )
}