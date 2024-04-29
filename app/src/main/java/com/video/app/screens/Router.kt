package com.video.app.screens

var idIncrement = 0;
fun getIdIn(): Int {
    return idIncrement++;
}

sealed class Router(val id: Int, val route: String) {
    data object HomeScreen : Router(getIdIn(), "home")
    data object SettingScreen : Router(getIdIn(), "setting")
    data object LoginScreen : Router(getIdIn(), "login")
    data object RegisterScreen : Router(getIdIn(), "register")
    data object ProfileScreen : Router(getIdIn(), "profile")
    data object SearchScreen:Router(getIdIn(),"search")
    data object ReportScreen:Router(getIdIn(),"report")
    data object OTPScreen:Router(getIdIn(),"otp"){
        fun setArgs(email:String,token:String):String{
            return "${route}/${email}/${token}"
        }
    }
    data object VIPRegisterScreen:Router(getIdIn(),"vip")
}