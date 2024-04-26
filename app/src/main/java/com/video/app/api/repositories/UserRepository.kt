package com.video.app.api.repositories

import com.video.app.api.ApiResponse
import com.video.app.api.ResponseLayout
import com.video.app.api.models.LoginRequest
import com.video.app.api.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserRepository {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<ResponseLayout<LoginResponse>>
}