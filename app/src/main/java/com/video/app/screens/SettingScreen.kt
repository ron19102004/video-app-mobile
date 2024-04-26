package com.video.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.Navigate
import com.video.app.config.CONSTANT
import com.video.app.screens.components.BtnText
import com.video.app.screens.components.Heading
import com.video.app.screens.components.SwitchComponent
import com.video.app.screens.layouts.MainLayout
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.ColorCustom

class SettingScreen {
    private lateinit var userViewModel: UserViewModel;

    @Composable
    fun Screen(userViewModel: UserViewModel) {
        this.userViewModel = userViewModel;
        MainLayout(userViewModel = userViewModel) {
            Heading(text = "Settings", size = CONSTANT.UI.TEXT_SIZE.XL)
            Divider()
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    ItemContainer(
                        contentLeft = {
                            Text(
                                text = "Dark mode",
                                modifier = Modifier.padding(10.dp, 0.dp),
                                style = TextStyle(
                                    fontSize = CONSTANT.UI.TEXT_SIZE.SM,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        },
                        contentRight = {
                            SwitchComponent(
                                modifier = Modifier.padding(10.dp, 0.dp),
                                checked = UiState.darkMode,
                                onCheckedChange = {
                                    UiState.changeDarkMode(it)
                                }
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (userViewModel.isLoggedIn)
                        BtnText(onClick = { userViewModel.logout() }, text = "Logout")
                    else
                        BtnText(onClick = { Navigate(Router.LoginScreen) }, text = "Login")
                }
            }
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
                    color = if (UiState.darkMode) ColorCustom.bgContainer_dark else ColorCustom.bgContainer_light,
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

@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    SettingScreen().Screen(userViewModel = viewModel())
}