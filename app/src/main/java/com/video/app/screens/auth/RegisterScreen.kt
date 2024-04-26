package com.video.app.screens.auth

import androidx.compose.runtime.Composable
import com.video.app.states.viewmodels.UserViewModel

class RegisterScreen {
    private lateinit var userViewModel: UserViewModel;

    @Composable
    fun Screen(userViewModel: UserViewModel) {
        this.userViewModel = userViewModel;
    }
}