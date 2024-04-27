package com.video.app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.video.app.R
import com.video.app.config.CONSTANT
import com.video.app.screens.components.Heading
import com.video.app.screens.layouts.MainLayout
import com.video.app.states.viewmodels.UserViewModel

class ProfileScreen {
    @Composable
    fun Screen(userViewModel: UserViewModel) {
        if (userViewModel.isLoggedIn) {
            val userCurrent = userViewModel.userCurrent.asFlow().collectAsState(initial = null)
            if (userViewModel != null) {
                val name = userCurrent.value?.fullName?.split(" ");
                MainLayout(userViewModel = userViewModel) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val painterModifier = Modifier.size(30.dp)
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = painterModifier) {
                                Image(
                                    painter = painterResource(id = R.drawable.user_icon),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = painterModifier
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Heading(text = "Hello ${name?.get(name.size-1)}", size = CONSTANT.UI.TEXT_SIZE.XL)
                        }
                        IconButton(onClick = { userViewModel.logout() }) {
                            Box(modifier = painterModifier) {
                                Image(
                                    painter = painterResource(id = R.drawable.logout_icon),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = painterModifier
                                )
                            }
                        }
                    }
                    Divider()
                }
            } else {
                MainLayout(userViewModel = userViewModel) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Heading(text = "An error has occurred", size = CONSTANT.UI.TEXT_SIZE.MD)
                        Spacer(modifier = Modifier.height(10.dp))
                        ElevatedButton(onClick = { userViewModel.logout() }) {
                            Text(text = "Logout")
                        }
                    }
                }
            }
        }
    }
}