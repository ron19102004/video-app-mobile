package com.video.app.screens.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.video.app.Navigate
import com.video.app.config.CONSTANT
import com.video.app.screens.Router
import com.video.app.states.objects.NavigationState
import com.video.app.states.viewmodels.UserViewModel

@Composable
fun NavigationBarBottom(userViewModel: UserViewModel) {
    NavigationBar {
        NavigationState.listItem.forEachIndexed { index, item ->
            NavigationBarItem(
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
                        contentDescription = null
                    )
                }
            )
        }
    }
}