package com.video.app.api.models

import com.squareup.moshi.Json
import java.util.Date

//DEFINE MODEL
class UserModel(
    var id: Long? = null,
    var fullName: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var username: String? = null,
    var role: String? = null,
    var confirmed: Boolean? = null,
    var isTwoFactorAuthentication: Boolean? = null,
    var imageURL: String? = null
)

class VIP(
    var id: Long? = null,
    var active: Boolean? = false,
    var issuedAt: String? = null,
    var expiredAt: String? = null
)

//DTO
data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    @Json(name = "token")
    val token: String,
    val user: UserModel,
    @Json(name = "TFA")
    val tfa: Boolean,
    val vip: VIP? = null
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val fullName: String,
    val email: String,
    val phone: String
)

data class VerifyOTPRequest(
    val otp: String,
    val token: String
)

data class InfoUserResponse(
    var vip: VIP? = null,
    var user: UserModel? = null
)

data class InfoConfirmedLoggedInResponse(
    var user: UserModel? = null,
    var isSubscribing: Boolean? = false
)
data class ChangePasswordRequest(
    val passwordCurrent:String,
    val passwordNew:String
)
