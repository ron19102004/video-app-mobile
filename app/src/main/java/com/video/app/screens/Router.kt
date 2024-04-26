package com.video.app.screens

sealed class Router(val id: Int, val route: String) {
    data object HomeScreen : Router(1, "home")
    data object SettingScreen : Router(5, "setting")


    data object LoginScreen : Router(6, "login")
    data object RegisterScreen : Router(7, "register")
}