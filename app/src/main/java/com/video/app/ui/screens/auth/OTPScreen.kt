package com.video.app.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.api.models.VerifyOTPRequest
import com.video.app.config.CONSTANT
import com.video.app.states.objects.AppInitializerState
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.layouts.AuthLayout
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

class OTPScreen {
    @Composable
    fun Screen(userViewModel: UserViewModel = AppInitializerState.userViewModel, email: String, token: String) {
        var text1 by remember { mutableStateOf("") }
        var text2 by remember { mutableStateOf("") }
        var text3 by remember { mutableStateOf("") }
        var text4 by remember { mutableStateOf("") }
        var text5 by remember { mutableStateOf("") }
        var text6 by remember { mutableStateOf("") }

        val focusRequester2 = remember {
            FocusRequester()
        }
        val focusRequester3 = remember {
            FocusRequester()
        }
        val focusRequester4 = remember {
            FocusRequester()
        }
        val focusRequester5 = remember {
            FocusRequester()
        }
        val focusRequester6 = remember {
            FocusRequester()
        }
        var activeBtn by remember {
            mutableStateOf(true)
        }
        var activeSendAgainBtn by remember {
            mutableStateOf(true)
        }
        val activeBtnHandle: () -> Unit = {
            activeBtn = true
            activeSendAgainBtn = true
        }
        AuthLayout {
            LazyColumn(modifier = Modifier.padding(10.dp, 0.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Heading(text = "Verify OTP for Login", size = CONSTANT.UI.TEXT_SIZE.XL)
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(
                        text = "Please verify your account with the OTP we sent to $email",
                        textAlign = TextAlign.Center,
                        fontSize = CONSTANT.UI.TEXT_SIZE.MD,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InputOTP(
                            value = text1,
                            onValueChange = {
                                text1 = it
                            },
                            focusRequester = FocusRequester.Default,
                            requestFocus = { focusRequester2.requestFocus() }
                        )
                        InputOTP(
                            value = text2,
                            onValueChange = {
                                text2 = it
                            },
                            focusRequester = focusRequester2,
                            requestFocus = { focusRequester3.requestFocus() }
                        )
                        InputOTP(
                            value = text3,
                            onValueChange = {
                                text3 = it
                            },
                            focusRequester = focusRequester3,
                            requestFocus = { focusRequester4.requestFocus() }
                        )
                        InputOTP(
                            value = text4,
                            onValueChange = {
                                text4 = it
                            },
                            focusRequester = focusRequester4,
                            requestFocus = { focusRequester5.requestFocus() }
                        )
                        InputOTP(
                            value = text5,
                            onValueChange = {
                                text5 = it
                            },
                            focusRequester = focusRequester5,
                            requestFocus = { focusRequester6.requestFocus() }
                        )
                        InputOTP(
                            value = text6,
                            onValueChange = {
                                text6 = it
                            },
                            focusRequester = focusRequester6
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ElevatedButton(
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = AppColor.background_container
                            ),
                            onClick = {
                                activeBtn = false
                                activeSendAgainBtn = false
                                val otp: String = text1 + text2 + text3 + text4 + text5 + text6
                                userViewModel.verifyOTP(
                                    VerifyOTPRequest(otp, token),
                                    activeBtnHandle
                                )
                            }, modifier = Modifier.height(50.dp),
                            enabled = activeBtn
                        ) {
                            Text(
                                text = "Verify", style = TextStyle(
                                    fontStyle = FontStyle.Normal,
                                    fontSize = CONSTANT.UI.TEXT_SIZE.MD,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColor.primary_content
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        TextButton(onClick = {
                            activeBtn = false
                            activeSendAgainBtn = false
                            userViewModel.sendOTP(
                                token,
                                activeBtnHandle
                            )
                        }, enabled = activeSendAgainBtn) {
                            Text(text = "Send again?", color = AppColor.primary_text)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun InputOTP(
        value: String,
        onValueChange: (String) -> Unit,
        focusRequester: FocusRequester,
        requestFocus: () -> Unit? = { }
    ) {
        OutlinedTextField(
            value = value, onValueChange = {
                onValueChange(it)
                if (it.isNotBlank() && it.isNotEmpty()) {
                    requestFocus()
                }
            },
            modifier = Modifier
                .width(50.dp)
                .height(70.dp)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
            ),
            textStyle = TextStyle(
                fontSize = CONSTANT.UI.TEXT_SIZE.MD,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = AppColor.primary_text
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OTPPreview() {
    OTPScreen().Screen(userViewModel = viewModel(), email = "admin@gmail.com", token = "")
}