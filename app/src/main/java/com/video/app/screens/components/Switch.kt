package com.video.app.screens.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.video.app.states.objects.UiState
import com.video.app.ui.theme.md_theme_dark_inversePrimary

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
            checkedTrackColor = md_theme_dark_inversePrimary,
            checkedThumbColor = Color.White
        ),
        modifier = modifier
    )
}