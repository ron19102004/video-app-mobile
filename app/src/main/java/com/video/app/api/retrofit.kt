package com.video.app.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

sealed class URL(val value: String) {
    data object BASE : URL("https://40ad-171-225-185-116.ngrok-free.app/")
}

object RetrofitAPI {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun service(url: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(url)
            .build()
    }
}

data class ApiResponse<T>(
    val data: T? = null,
    val error: String? = "Error"
)

data class ResponseLayout<DTO>(
    val message: String,
    val data: DTO,
    val status: Boolean
)