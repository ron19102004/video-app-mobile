package com.video.app.ui.screens.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.video.app.ui.screens.components.NavigationBarBottom
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

@Composable
fun MainLayout(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier.padding(10.dp, 0.dp),
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor = AppColor.background,
        bottomBar = { NavigationBarBottom(userViewModel) }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            content()
        }
    }
}