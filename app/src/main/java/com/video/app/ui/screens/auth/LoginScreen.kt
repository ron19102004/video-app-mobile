package com.video.app.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.LoginRequest
import com.video.app.config.CONSTANT
import com.video.app.ui.screens.Router
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Input
import com.video.app.ui.screens.layouts.AuthLayout
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

class LoginScreen {
    @Composable
    fun Screen(userViewModel: UserViewModel) {
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
        var enabledButton by remember {
            mutableStateOf(true)
        }
        val activeButton: () -> Unit = {
            enabledButton = true
        }
        //UI
        AuthLayout {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp, 0.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.video_bg),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text(
                        text = "Log In to ${stringResource(id = R.string.app_name)}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = CONSTANT.UI.TEXT_SIZE.XL,
                            textAlign = TextAlign.Center,
                            color = AppColor.primary_text
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
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
                        enabled = enabledButton,
                        onClick = {
                            if (username.isNotBlank() && password.isNotBlank() && password.length >= 8) {
                                enabledButton = false
                                Toast.makeText(userViewModel.context, "Logging...", Toast.LENGTH_SHORT).show()
                                userViewModel.login(
                                    LoginRequest(
                                        username,
                                        password
                                    ), activeButton
                                )
                            } else validate()
                        }, text = "sign in".uppercase()
                    )
                    TextButton(
                        onClick = { Navigate(Router.RegisterScreen) },
                        enabled = enabledButton
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Don't have an account? ",
                                color = AppColor.primary_text
                            )
                            Text(text = "Register here", color = AppColor.primary_content)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen().Screen(userViewModel = viewModel())
}