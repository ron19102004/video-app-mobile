package com.video.app.states.objects

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.video.app.screens.Router

object NavigationState {
    var navSelected by mutableIntStateOf(0)
    val listItem: List<String> = listOf("Home", "Profile", "Settings")
    val listIcon: List<ImageVector> =
        listOf(Icons.Rounded.Home, Icons.Rounded.Person, Icons.Rounded.Settings)
    val listRouter: List<Router> =
        listOf(Router.HomeScreen, Router.ProfileScreen, Router.SettingScreen)
}