package com.video.app.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.api.models.LoginRequest
import com.video.app.screens.components.BtnText
import com.video.app.screens.components.Input
import com.video.app.states.viewmodels.UserViewModel

class LoginScreen {
    private lateinit var userViewModel: UserViewModel;

    private fun onSubmit(loginRequest: LoginRequest) {
        this.userViewModel.login(loginRequest)
    }

    @Composable
    fun Screen(userViewModel: UserViewModel) {
        this.userViewModel = userViewModel;

        var username by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        var usernameIsError by remember {
            mutableStateOf(false)
        }
        var passwordIsError by remember {
            mutableStateOf(false)
        }
        var usernameErrorMessage by remember {
            mutableStateOf("")
        }
        var passwordErrorMessage by remember {
            mutableStateOf("")
        }
        val validate: () -> Unit = {
            if (username.isBlank()) {
                usernameIsError = true;
                usernameErrorMessage = "Email or phone must not empty"
            }
            if (password.isBlank()) {
                passwordIsError = true;
                passwordErrorMessage = "Password must not empty"
            } else {
                if (password.length < 8) {
                    passwordIsError = true;
                    passwordErrorMessage = "Password must the least 8 characters"
                }
            }
        }
        LazyColumn {
            item {
                Input(
                    value = username,
                    onValueChange = {
                        username = it;
                        usernameIsError = false
                    },
                    placeholder = "user12345",
                    label = "Email or phone",
                    isError = usernameIsError,
                    errorMessage = usernameErrorMessage
                )
                Spacer(modifier = Modifier.height(10.dp))
                Input(
                    value = password,
                    onValueChange = {
                        password = it;
                        passwordIsError = false
                    },
                    placeholder = "********",
                    label = "Password",
                    isPassword = true,
                    isError = passwordIsError,
                    errorMessage = passwordErrorMessage
                )
                Spacer(modifier = Modifier.height(10.dp))

                BtnText(
                    onClick = {
                        if (username.isNotBlank() && password.isNotBlank() && password.length >= 8)
                            onSubmit(
                                LoginRequest(
                                    username,
                                    password
                                )
                            )
                        else validate()
                    }, text = "Login"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen().Screen(userViewModel = viewModel())
}