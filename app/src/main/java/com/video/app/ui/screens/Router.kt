package com.video.app.ui.screens

private var idIncrement = 0;
fun getIdIn(): Int {
    return idIncrement++;
}

sealed class Router(val id: Int, val route: String) {
    data object HomeScreen : Router(getIdIn(), "home")
    data object SettingScreen : Router(getIdIn(), "setting")
    data object LoginScreen : Router(getIdIn(), "login")
    data object RegisterScreen : Router(getIdIn(), "register")
    data object MyProfileScreen : Router(getIdIn(), "my_profile")
    data object YourProfileScreen : Router(getIdIn(), "ur_profile") {
        fun setArgs(userId: Long): String {
            return "$route/${userId}";
        }
    }

    data object SearchScreen : Router(getIdIn(), "search")
    data object ReportScreen : Router(getIdIn(), "report")
    data object OTPScreen : Router(getIdIn(), "otp") {
        fun setArgs(email: String, token: String): String {
            return "${route}/${email}/${token}"
        }
    }

    data object VIPRegisterScreen : Router(getIdIn(), "vip")
    data object VideoPlayerScreen : Router(getIdIn(), "play") {
        fun setArgs(index: Int, videoAt: String, uploaderId: Long): String {
            return "$route/${index}/${uploaderId}/${videoAt}"
        }
    }

    data object PlaylistVideoScreen : Router(getIdIn(), "playlist_video") {
        fun setArgs(playlistId: Long, playlistIndex: Int): String {
            return "$route/${playlistId}/${playlistIndex}"
        }
    }
}