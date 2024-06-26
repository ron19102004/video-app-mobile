package com.video.app.ui.screens

import com.video.app.api.models.VideoModel
import com.video.app.states.objects.AppInitializerState
import com.video.app.states.objects.NavigationState
import kotlinx.serialization.Serializable

fun Navigate(router: Router) {
    when (router) {
        is Router.HomeScreen -> {
            NavigationState.navSelected = 1
        }

        is Router.SettingScreen -> {
            NavigationState.navSelected = 2
        }

        is Router.MyProfileScreen -> {
            NavigationState.navSelected = 0
        }

        else -> {}
    }
    AppInitializerState.navController.navigate(router)
}

sealed class Router {
    @Serializable
    data object HomeScreen : Router()

    @Serializable
    data object SettingScreen : Router()

    @Serializable
    data object LoginScreen : Router()

    @Serializable
    data object RegisterScreen : Router()

    @Serializable
    data object MyProfileScreen : Router()

    @Serializable
    data class UserProfileScreen(
        val userId: Long
    ) : Router()

    @Serializable
    data object SearchScreen : Router()

    @Serializable
    data object ReportScreen : Router()

    @Serializable
    data class OTPScreen(
        val email: String,
        val token: String
    ) : Router()

    @Serializable
    data object VIPRegisterScreen : Router()

    @Serializable
    data class VideoPlayerScreen(
        val index: Int,
        val videoAt: String,
        val uploaderId: Long,
        val playlistAt: String? = null
    ) : Router()

    @Serializable
    data class PlaylistVideoScreen(
        val playlistAt: String,
        val playlistId: Long,
        val playlistIndex: Int,
    ) : Router()

    @Serializable
    data object UpdateAvatarScreen : Router()

    @Serializable
    data object MyVideoScreen : Router()
}