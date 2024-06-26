package com.video.app.api

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.video.app.states.viewmodels.SharedPreferencesAuthKey
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.system.exitProcess

@SuppressLint("StaticFieldLeak")
object RetrofitAPI {
    private lateinit var _context: Context
    fun init(context: Context) {
        _context = context;
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            if (!isTokenRequired(originalRequest)) {
                return@addInterceptor chain.proceed(originalRequest)
            }
            val token =
                _context.getSharedPreferences(SharedPreferencesAuthKey.ROOT, Context.MODE_PRIVATE)
                    .getString(SharedPreferencesAuthKey.ACCESS_TOKEN, "");
            if (!token.isNullOrBlank()) {
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            return@addInterceptor chain.proceed(originalRequest)
        }
        .build()

    private fun isTokenRequired(request: Request): Boolean {
        val path = request.url.encodedPath
        return when (path) {
            "/users/info",
            "/auth/change-TFA",
            "/users/vip/register",
            "/users/vip/cancel",
            "/users/image",
            "/users/change-password",
            "/playlists/my",
            "/playlists/new",
            "/playlists/videos",
            "/playlists/add",
            "/playlists/video",
            "/playlists",
            "/users/loggedIn/info-confirmed",
            "/users/unsubscribe",
            "/users/subscribe",
            "/videos/my-videos",
            "/comments/new"-> true

            else -> false
        }
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun service(url: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(url)
            .client(client)
            .build()
    }
}

data class ResponseLayout<DTO>(
    val message: String,
    val data: DTO? = null,
    val status: Boolean
)