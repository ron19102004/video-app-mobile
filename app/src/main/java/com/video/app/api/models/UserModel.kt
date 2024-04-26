package com.video.app.api.models

import com.squareup.moshi.Json

//DEFINE MODEL
class UserModel(
    var id: Long,
    var fullName: String,
    val phone: String,
    val email: String,
    val username: String,
    val role: String,
    val confirmed: Boolean
)
//DTO
data class LoginRequest(
    val username: String,
    val password: String
)
data class LoginResponse(
    @Json(name = "access-token")
    val accessToken:String,
    val user: UserModel,
)
data class RegisterRequest(
    val username: String,
    val password: String,
    val fullName:String,
    val email:String,
    val phone:String
)
