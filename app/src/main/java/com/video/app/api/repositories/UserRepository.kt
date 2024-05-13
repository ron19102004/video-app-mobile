package com.video.app.api.repositories

import com.video.app.api.ApiResponse
import com.video.app.api.ResponseLayout
import com.video.app.api.models.InfoConfirmedLoggedInResponse
import com.video.app.api.models.InfoUserResponse
import com.video.app.api.models.LoginRequest
import com.video.app.api.models.LoginResponse
import com.video.app.api.models.RegisterRequest
import com.video.app.api.models.UserModel
import com.video.app.api.models.VIP
import com.video.app.api.models.VerifyOTPRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserRepository {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<ResponseLayout<LoginResponse>>

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @GET("users/info")
    fun info(): Call<ResponseLayout<InfoUserResponse>>

    @Multipart
    @PATCH("users/image")
    fun updateAvatar(@Part file: MultipartBody.Part): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @POST("auth/verify-OTP")
    fun verifyOTP(@Body verifyOTPRequest: VerifyOTPRequest): Call<ResponseLayout<LoginResponse?>>

    @Headers("Content-Type: application/json")
    @POST("auth/send-OTP")
    fun sendOTP(@Query("token") token: String): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @POST("auth/change-TFA")
    fun changeTFA(): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @POST("users/vip/register")
    fun registerVIP(@Query("month") month: Int): Call<ResponseLayout<VIP?>>

    @Headers("Content-Type: application/json")
    @DELETE("users/vip/cancel")
    fun cancelVIP(): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @GET("users/info-confirmed")
    fun getInfoUserConfirmed(@Query("id") id: Long): Call<ResponseLayout<UserModel?>>

    @Headers("Content-Type: application/json")
    @GET("users/loggedIn/info-confirmed")
    fun getInfoUserConfirmedWhenLoggedIn(@Query("id") id: Long): Call<ResponseLayout<InfoConfirmedLoggedInResponse?>>

    @Headers("Content-Type: application/json")
    @POST("users/subscribe")
    fun subscribe(@Query("id") id: Long): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @DELETE("users/unsubscribe")
    fun unsubscribe(@Query("id") id: Long): Call<ResponseLayout<Any>>

}