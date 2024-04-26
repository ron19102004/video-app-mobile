package com.video.app.screens.layouts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.video.app.screens.components.NavigationBarBottom
import com.video.app.states.viewmodels.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainLayout(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier.padding(10.dp, 0.dp),
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { NavigationBarBottom(userViewModel) }
    ) {
        Column(modifier = modifier
            .fillMaxSize()
            .padding(it)) {
            content()
        }
    }
}