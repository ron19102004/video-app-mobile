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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.LoginRequest
import com.video.app.api.models.RegisterRequest
import com.video.app.config.CONSTANT
import com.video.app.config.ValidRegex
import com.video.app.ui.screens.Router
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Input
import com.video.app.ui.screens.layouts.AuthLayout
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

class RegisterScreen {
    @Composable
    fun Screen(userViewModel: UserViewModel) {
        //data
        var username by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        var phone by remember {
            mutableStateOf("")
        }
        var email by remember {
            mutableStateOf("")
        }
        var fullName by remember {
            mutableStateOf("")
        }
        var rePassword by remember {
            mutableStateOf("")
        }
        //is error
        var usernameIsError by remember {
            mutableStateOf(false)
        }
        var passwordIsError by remember {
            mutableStateOf(false)
        }
        var rePasswordIsError by remember {
            mutableStateOf(false)
        }
        var phoneIsError by remember {
            mutableStateOf(false)
        }
        var emailIsError by remember {
            mutableStateOf(false)
        }
        var fullNameIsError by remember {
            mutableStateOf(false)
        }
        //error message
        var usernameErrorMessage by remember {
            mutableStateOf("")
        }
        var passwordErrorMessage by remember {
            mutableStateOf("")
        }
        var rePasswordErrorMessage by remember {
            mutableStateOf("")
        }
        var phoneErrorMessage by remember {
            mutableStateOf("")
        }
        var emailErrorMessage by remember {
            mutableStateOf("")
        }
        var fullNameErrorMessage by remember {
            mutableStateOf("")
        }
        val validate: () -> Unit = {
            if (username.isBlank()) {
                usernameIsError = true;
                usernameErrorMessage = "Username must not empty"
            }
            if (phone.isBlank()) {
                phoneIsError = true;
                phoneErrorMessage = "Phone must not empty"
            } else {
                if (phone.length < 10) {
                    phoneIsError = true;
                    phoneErrorMessage = "Phone must the least 10 numbers"
                } else if (phone.length > 11) {
                    phoneIsError = true;
                    phoneErrorMessage = "Phone maximum is 11 numbers "
                }
            }
            if (fullName.isBlank()) {
                fullNameIsError = true;
                fullNameErrorMessage = "Full name must not empty"
            }
            if (email.isBlank()) {
                emailIsError = true;
                emailErrorMessage = "Email must not empty"
            } else if (!ValidRegex.isEmail(email)) {
                emailIsError = true;
                emailErrorMessage = "Email not valid"
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
            if (rePassword.isBlank()) {
                rePasswordIsError = true;
                rePasswordErrorMessage = "Re-Password must not empty"
            } else {
                if (password != rePassword) {
                    rePasswordIsError = true;
                    rePasswordErrorMessage = "Re-Password incorrect"
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
                            painter = painterResource(id = R.drawable.video_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .width(90.dp)
                                .height(90.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text(
                        text = "Create an account ${stringResource(id = R.string.app_name)}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = CONSTANT.UI.TEXT_SIZE.LG,
                            textAlign = TextAlign.Center,
                            color = AppColor.primary_text
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Input(
                        value = fullName,
                        onValueChange = {
                            fullName = it;
                            fullNameIsError = false
                        },
                        placeholder = "Tran Dinh A",
                        label = "Full name",
                        isError = fullNameIsError,
                        errorMessage = fullNameErrorMessage
                    )
                    Input(
                        value = username,
                        onValueChange = {
                            username = it;
                            usernameIsError = false
                        },
                        placeholder = "user12345",
                        label = "Username",
                        isError = usernameIsError,
                        errorMessage = usernameErrorMessage
                    )
                    Input(
                        value = email,
                        onValueChange = {
                            email = it;
                            emailIsError = false
                        },
                        placeholder = "example@gmail.com",
                        label = "Email",
                        isError = emailIsError,
                        errorMessage = emailErrorMessage,
                        keyboardType = KeyboardType.Email
                    )
                    Input(
                        value = phone,
                        onValueChange = {
                            phone = it;
                            phoneIsError = false
                        },
                        placeholder = "0392477615",
                        label = "Phone number",
                        isError = phoneIsError,
                        errorMessage = phoneErrorMessage,
                        keyboardType = KeyboardType.Phone
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
                    Input(
                        value = rePassword,
                        onValueChange = {
                            rePassword = it;
                            if (password != rePassword) {
                                rePasswordIsError = true;
                                rePasswordErrorMessage = "Password incorrect"
                            } else rePasswordIsError = false
                        },
                        placeholder = "********",
                        label = "Re-password",
                        isPassword = true,
                        isError = rePasswordIsError,
                        errorMessage = rePasswordErrorMessage
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BtnText(
                        enabled = enabledButton,
                        onClick = {
                            if (username.isNotBlank() &&
                                password.isNotBlank() &&
                                phone.isNotBlank() &&
                                email.isNotBlank() &&
                                fullName.isNotBlank() &&
                                (phone.length in 10..11) &&
                                ValidRegex.isEmail(email) &&
                                password.length >= 8
                            ) {
                                enabledButton = false;
                                Toast.makeText(
                                    userViewModel.context,
                                    "Logging...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                userViewModel.register(
                                    RegisterRequest(
                                        username, password, fullName, email, phone
                                    ), activeButton
                                )
                            } else validate()
                        },
                        text = "sign up".uppercase(),

                        )
                    TextButton(
                        onClick = { Navigate(Router.LoginScreen) },
                        enabled = enabledButton
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Already an account? ",
                                color = AppColor.primary_text
                            )
                            Text(text = "Login here", color = AppColor.primary_content)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterScreen().Screen(userViewModel = viewModel())
}