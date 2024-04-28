package com.video.app.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.video.app.Navigate
import com.video.app.config.CONSTANT
import com.video.app.screens.Router
import com.video.app.states.objects.NavigationState
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.ColorCustom

@Composable
fun NavigationBarBottom(userViewModel: UserViewModel) {
    Column {
        Divider()
        NavigationBar(
            containerColor = if (UiState.darkMode) Color.Black
            else MaterialTheme.colorScheme.onPrimary
        ) {
            NavigationState.listItem.forEachIndexed { index, item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = if (UiState.darkMode) ColorCustom.bgContainer_dark
                        else ColorCustom.bgContainer_light
                    ),
                    selected = NavigationState.listRouter[index].id == NavigationState.navSelected,
                    label = {
                        Text(
                            text = item, style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = CONSTANT.UI.TEXT_SIZE.SM
                            )
                        )
                    },
                    onClick = {
                        if (NavigationState.listRouter[index].id == Router.ProfileScreen.id && !userViewModel.isLoggedIn) {
                            Navigate(Router.LoginScreen.route)
                        } else {
                            NavigationState.navSelected = NavigationState.listRouter[index].id
                            Navigate(NavigationState.listRouter[index])
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = NavigationState.listIcon[index],
                            contentDescription = null,
                            tint = if (NavigationState.listRouter[index].id == NavigationState.navSelected) {
                                if (UiState.darkMode) Color.White
                                else Color.Black
                            } else {
                                if (UiState.darkMode) Color.DarkGray
                                else Color.LightGray
                            }
                        )
                    }
                )
            }
        }
    }
}