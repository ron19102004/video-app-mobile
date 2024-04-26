package com.video.app.screens

import androidx.compose.runtime.Composable
import com.video.app.states.viewmodels.UserViewModel

class HomeScreen {
    private lateinit var userViewModel: UserViewModel;
    @Composable
    fun Screen(userViewModel: UserViewModel) {
        this.userViewModel = userViewModel;
    }
}