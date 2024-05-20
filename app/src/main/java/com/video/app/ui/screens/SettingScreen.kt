package com.video.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.R
import com.video.app.api.URL
import com.video.app.api.models.ChangePasswordRequest
import com.video.app.config.CONSTANT
import com.video.app.states.objects.AppInitializerState
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.components.Input
import com.video.app.ui.screens.components.SwitchComponent
import com.video.app.ui.screens.layouts.MainLayout
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.launch

class SettingScreen {
    private lateinit var userViewModel: UserViewModel
    private var openChangePasswordModal = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(userViewModel: UserViewModel = AppInitializerState.userViewModel) {
        this.userViewModel = userViewModel
        MainLayout(userViewModel = userViewModel) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painterModifier = Modifier.size(30.dp)
                Box(modifier = painterModifier) {
                    Image(
                        painter = painterResource(id = R.drawable.settings_icon),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = painterModifier
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Heading(text = "Settings", size = CONSTANT.UI.TEXT_SIZE.XL)
            }
            HorizontalDivider(color = AppColor.background_container)
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (userViewModel.isLoggedIn) {
                        ItemContainer(
                            contentLeft = {
                                TextIconImage(
                                    text = "TFA",
                                    painter = painterResource(id = R.drawable.otp_icon)
                                )
                            },
                            contentRight = {
                                SwitchComponent(
                                    modifier = Modifier.padding(10.dp, 0.dp),
                                    checked = userViewModel.tfa,
                                    onCheckedChange = {
                                        userViewModel.changeTFA(it)
                                    }
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ItemContainer(
                            contentLeft = {
                                TextIconImage(
                                    text = "Change password",
                                    painter = painterResource(id = R.drawable.password_icon)
                                )
                            },
                            contentRight = {
                                IconButton(onClick = { openChangePasswordModal.value = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.Build,
                                        contentDescription = null,
                                        tint = AppColor.primary_text
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        BtnText(onClick = { userViewModel.logout() }, text = "Logout")
                    } else
                        BtnText(onClick = { Navigate(Router.LoginScreen) }, text = "Login")
                    Spacer(modifier = Modifier.height(10.dp))
                    TextButton(
                        onClick = { Navigate(Router.ReportScreen) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Report here", color = AppColor.primary_content)
                    }
                }
            }
        }
        ModalBottomSheetsContainer()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ModalBottomSheetsContainer() {
        val openChangePasswordModalState = rememberModalBottomSheetState()
        if (openChangePasswordModal.value) {
            var passwordCurrent by remember {
                mutableStateOf("")
            }
            var passwordNew by remember {
                mutableStateOf("")
            }
            var rePasswordNew by remember {
                mutableStateOf("")
            }
            var isErrorPasswordCurrent by remember {
                mutableStateOf(false)
            }
            var isErrorPasswordNew by remember {
                mutableStateOf(false)
            }
            var isErrorRePasswordNew by remember {
                mutableStateOf(false)
            }
            var passwordCurrentMessage by remember {
                mutableStateOf("")
            }
            var passwordNewMessage by remember {
                mutableStateOf("")
            }
            var rePasswordNewMessage by remember {
                mutableStateOf("")
            }

            var errorValid by remember {
                mutableStateOf(false)
            }
            val validate: () -> Unit = {
                if (passwordCurrent.isBlank()) {
                    isErrorPasswordCurrent = true
                    passwordCurrentMessage = "Password current must not blank"
                    errorValid = true
                } else {
                    if (passwordCurrent.length < 8) {
                        isErrorPasswordCurrent = true
                        passwordCurrentMessage = "Password current must at least 8 characters"
                        errorValid = true
                    }
                }
                if (passwordNew.isBlank()) {
                    isErrorPasswordNew = true
                    passwordNewMessage = "Password new must not blank"
                    errorValid = true
                } else {
                    if (passwordNew.length < 8) {
                        isErrorPasswordNew = true
                        passwordNewMessage = "Password new must at least 8 characters"
                        errorValid = true
                    }
                }
                if (rePasswordNew.isBlank()) {
                    isErrorRePasswordNew = true
                    rePasswordNewMessage = "Re password new must not blank"
                    errorValid = true
                } else {
                    if (rePasswordNew.length < 8) {
                        isErrorRePasswordNew = true
                        rePasswordNewMessage = "Re password new must at least 8 characters"
                        errorValid = true
                    } else {
                        if (passwordNew != rePasswordNew) {
                            isErrorRePasswordNew = true
                            rePasswordNewMessage = "Re password new must same with password new"
                            errorValid = true
                        }
                    }
                }
            }
            ModalBottomSheet(
                onDismissRequest = { openChangePasswordModal.value = false },
                sheetState = openChangePasswordModalState,
                containerColor = AppColor.background,

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Heading(text = "Change password", size = CONSTANT.UI.TEXT_SIZE.LG)
                    Input(
                        value = passwordCurrent,
                        onValueChange = {
                            isErrorPasswordCurrent = false
                            passwordCurrent = it
                        },
                        label = "Password current",
                        isPassword = true,
                        isError = isErrorPasswordCurrent,
                        errorMessage = passwordCurrentMessage
                    )
                    Input(
                        value = passwordNew,
                        onValueChange = {
                            isErrorPasswordNew = false
                            passwordNew = it
                        },
                        isPassword = true,
                        label = "Password new",
                        isError = isErrorPasswordNew,
                        errorMessage = passwordNewMessage
                    )
                    Input(
                        value = rePasswordNew,
                        onValueChange = {
                            isErrorRePasswordNew = false
                            rePasswordNew = it
                        },
                        isPassword = true,
                        label = "Re-password new",
                        isError = isErrorRePasswordNew,
                        errorMessage = rePasswordNewMessage
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BtnText(
                        onClick = {
                            validate()
                            if (!errorValid) {
                                userViewModel.changePassword(
                                    ChangePasswordRequest(
                                        passwordCurrent = passwordCurrent,
                                        passwordNew = passwordNew
                                    ),
                                    actionSuccess = {
                                        passwordCurrent = ""
                                        passwordNew = ""
                                        rePasswordNew = ""
                                        openChangePasswordModal.value = false
                                    }
                                )
                            }
                        },
                        text = "Change",
                        buttonColor = AppColor.primary_content
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }

    @Composable
    private fun DevTools(hideModal: () -> Unit) {
        var urlApi by remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Input(
                value = urlApi,
                onValueChange = {
                    urlApi = it
                }, placeholder = "https://example.api.com/", label = "URL API"
            )
            Spacer(modifier = Modifier.height(10.dp))
            BtnText(onClick = {
                if (urlApi.isBlank()) {
                    Toast.makeText(
                        userViewModel.context,
                        "URL must not blank",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    URL.save(urlApi)
                    hideModal()
                    Toast.makeText(
                        userViewModel.context,
                        "URL is changed! Off and on app again!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }, text = "Change")
        }
    }

    @Composable
    private fun TextIconImage(
        text: String,
        modifier: Modifier = Modifier.padding(10.dp, 0.dp),
        textStyle: TextStyle = TextStyle(
            fontSize = CONSTANT.UI.TEXT_SIZE.SM,
            fontWeight = FontWeight.SemiBold,
            color = AppColor.primary_text
        ),
        textModifier: Modifier = Modifier.padding(5.dp, 0.dp),
        painter: Painter,
        painterModifier: Modifier = Modifier.size(20.dp)
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = painterModifier) {
                Image(
                    painter = painter, contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = painterModifier
                )
            }
            Text(
                text = text,
                modifier = textModifier,
                style = textStyle
            )
        }
    }

    @Composable
    private fun ItemContainer(
        modifier: Modifier = Modifier,
        contentLeft: @Composable () -> Unit,
        contentRight: @Composable () -> Unit,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(CONSTANT.UI.HEIGHT_BUTTON)
                .background(
                    color = AppColor.background_container,
                    shape = RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON)
                )
                .clip(shape = RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            contentLeft()
            contentRight()
        }
    }

}
