package com.video.app.states.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.video.app.api.ApiResponse
import com.video.app.api.ResponseLayout
import com.video.app.api.RetrofitAPI
import com.video.app.api.URL
import com.video.app.api.models.LoginRequest
import com.video.app.api.models.LoginResponse
import com.video.app.api.models.UserModel
import com.video.app.api.repositories.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserViewModel : ViewModel() {
    object SharedPreferencesKey {
        const val ROOT = "auth"
        const val ACCESS_TOKEN = "access-token"
        const val IS_LOGGED_IN = "isLoggedIn"
    }

    private val userRepository by lazy {
        RetrofitAPI.service(URL.BASE.value).create(UserRepository::class.java)
    }
    private lateinit var context: Context;
    private val _userCurrent = MutableLiveData<UserModel>();
    var isLoggedIn by mutableStateOf(false);
    var accessToken by mutableStateOf("");
    val userCurrent: LiveData<UserModel> = _userCurrent

    fun init(context: Context) {
        this.context = context;
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SharedPreferencesKey.ROOT, Context.MODE_PRIVATE)
        isLoggedIn = sharedPreferences.getBoolean(SharedPreferencesKey.IS_LOGGED_IN, false)
        accessToken = sharedPreferences.getString(SharedPreferencesKey.ACCESS_TOKEN, "").toString()
    }

    private fun saveSharedPreferences() {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SharedPreferencesKey.ROOT, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(SharedPreferencesKey.ACCESS_TOKEN, accessToken)
            .putBoolean(SharedPreferencesKey.IS_LOGGED_IN, isLoggedIn)
            .apply()
    }

    fun login(loginRequest: LoginRequest) {
        this.userRepository.login(loginRequest)!!
            .enqueue(object : Callback<ResponseLayout<LoginResponse>> {
                override fun onResponse(
                    call: Call<ResponseLayout<LoginResponse>>,
                    response: Response<ResponseLayout<LoginResponse>>
                ) {
                    if (response.isSuccessful) {
                        val res: ResponseLayout<LoginResponse>? = response.body()
                        if (res?.status == true) {
                            _userCurrent.value = res?.data?.user
                            isLoggedIn = true
                            accessToken = res?.data?.accessToken.toString()
                            saveSharedPreferences()
                        }
                        Toast.makeText(
                            context,
                            res?.message ?: "Login Data Error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseLayout<LoginResponse>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}