package com.video.app.api.models

import com.squareup.moshi.Json

//DEFINE MODEL
class UserModel(
    var id: Long,
    var fullName: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var username: String? = null,
    var role: String? = null,
    var confirmed: Boolean? = null,
    var isTwoFactorAuthentication: Boolean? = null
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
    val tfa: Boolean
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
data class VerifyOTPDataResponse(
    @Json(name = "token")
    val token: String,
    val user: UserModel,
)
