package com.video.app.screens.layouts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainLayout(content: @Composable () -> Unit) {
    Scaffold {
        Column {
            content()
        }
    }
}