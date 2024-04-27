package com.video.app.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.ReportModel
import com.video.app.config.CONSTANT
import com.video.app.config.ValidRegex
import com.video.app.navController
import com.video.app.screens.components.BtnText
import com.video.app.screens.components.Heading
import com.video.app.screens.components.Input
import com.video.app.states.viewmodels.UserViewModel

class ReportScreen {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Screen(userViewModel: UserViewModel) {
        var email by remember {
            mutableStateOf("")
        }
        var emailErrMess by remember {
            mutableStateOf("")
        }
        var emailIsErr by remember {
            mutableStateOf(false)
        }
        var content by remember {
            mutableStateOf("")
        }
        var contentErrMess by remember {
            mutableStateOf("")
        }
        var contentIsErr by remember {
            mutableStateOf(false)
        }
        var enabledButtonSubmit by remember {
            mutableStateOf(true)
        }
        val activeButtonSubmit: () -> Unit = {
            enabledButtonSubmit = false;
        }
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            }
        ) {
            LazyColumn(modifier = Modifier.padding(10.dp, it.calculateTopPadding())) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.video_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .clip(CircleShape)
                                .clickable { Navigate(Router.HomeScreen) },
                            contentScale = ContentScale.Fit
                        )
                        Heading(text = "Report issue", size = CONSTANT.UI.TEXT_SIZE.XL)
                    }
                    Input(
                        value = email,
                        onValueChange = {
                            email = it;
                            emailIsErr = false
                        },
                        keyboardType = KeyboardType.Email,
                        placeholder = "example@gmail.com",
                        label = "Your email",
                        isError = emailIsErr,
                        errorMessage = emailErrMess
                    )
                    Input(
                        value = content,
                        onValueChange = {
                            content = it;
                            contentIsErr = false
                        },
                        placeholder = "Problem...",
                        label = "Content",
                        singleLine = false,
                        height = 200.dp,
                        isError = contentIsErr,
                        errorMessage = contentErrMess
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BtnText(
                        enabled = enabledButtonSubmit,
                        onClick = {
                            if (email.isNotBlank() && content.isNotBlank() && ValidRegex.isEmail(
                                    email
                                )
                            ) {
                                Toast.makeText(
                                    userViewModel.context,
                                    "Reporting...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                enabledButtonSubmit = false
                                userViewModel.report(
                                    ReportModel(email, content),
                                    activeButtonSubmit
                                )
                            } else {
                                if (email.isBlank()) {
                                    emailIsErr = true
                                    emailErrMess = "Email must not blank"
                                } else if (!ValidRegex.isEmail(email)) {
                                    emailIsErr = true
                                    emailErrMess = "Email is invalid"
                                }
                                if (content.isBlank()) {
                                    contentIsErr = true
                                    contentErrMess = "Content must not blank"
                                } else if (content.length < 20) {
                                    contentIsErr = true
                                    contentErrMess = "Content must the lease 20 characters"
                                }
                            }
                        },
                        text = "Submit report"
                    )
                }
            }
        }
    }
}