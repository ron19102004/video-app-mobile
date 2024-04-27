package com.video.app.states.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.video.app.Navigate
import com.video.app.api.ResponseLayout
import com.video.app.api.RetrofitAPI
import com.video.app.api.URL
import com.video.app.api.models.LoginRequest
import com.video.app.api.models.LoginResponse
import com.video.app.api.models.RegisterRequest
import com.video.app.api.models.ReportModel
import com.video.app.api.models.UserModel
import com.video.app.api.repositories.ReportRepository
import com.video.app.api.repositories.UserRepository
import com.video.app.screens.Router
import com.video.app.services.NotificationService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

object SharedPreferencesAuthKey {
    const val ROOT = "auth"
    const val ACCESS_TOKEN = "access-token"
    const val IS_LOGGED_IN = "isLoggedIn"
}

class UserViewModel : ViewModel() {
    private val userRepository by lazy {
        RetrofitAPI.service(URL.BASE.value).create(UserRepository::class.java)
    }
    private val reportRepository by lazy {
        RetrofitAPI.service(URL.BASE.value).create(ReportRepository::class.java)
    }
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context;
    private lateinit var sharedPreferences: SharedPreferences;
    val userCurrent = MutableLiveData<UserModel?>(null);
    var isLoggedIn by mutableStateOf(false);
    var accessToken by mutableStateOf("");

    fun init(context: Context) {
        this.context = context;
        sharedPreferences =
            context.getSharedPreferences(SharedPreferencesAuthKey.ROOT, Context.MODE_PRIVATE)
        isLoggedIn =
            sharedPreferences.getBoolean(SharedPreferencesAuthKey.IS_LOGGED_IN, false)
        accessToken =
            sharedPreferences.getString(SharedPreferencesAuthKey.ACCESS_TOKEN, "").toString()
        if (isLoggedIn) loadUserFormToken()
    }

    private fun saveSharedPreferences() {
        sharedPreferences.edit()
            .putString(SharedPreferencesAuthKey.ACCESS_TOKEN, accessToken)
            .putBoolean(SharedPreferencesAuthKey.IS_LOGGED_IN, isLoggedIn)
            .apply()
    }

    private fun loadUserFormToken() {
        viewModelScope.launch {
            try {
                userRepository.info()!!.enqueue(object : Callback<ResponseLayout<UserModel>> {
                    override fun onResponse(
                        call: Call<ResponseLayout<UserModel>>,
                        response: Response<ResponseLayout<UserModel>>
                    ) {
                        if (response.isSuccessful) {
                            val res: ResponseLayout<UserModel>? = response.body()
                            userCurrent.value = res?.data
                        } else logout()
                    }

                    override fun onFailure(call: Call<ResponseLayout<UserModel>>, t: Throwable) {
                        Log.e("loadUserFormToken-error", t.message.toString())
                        Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG).show()
                    }

                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun login(loginRequest: LoginRequest, activeButton: () -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.login(loginRequest)!!
                    .enqueue(object : Callback<ResponseLayout<LoginResponse>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<LoginResponse>>,
                            response: Response<ResponseLayout<LoginResponse>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<LoginResponse>? = response.body()
                                if (res?.status == true) {
                                    userCurrent.value = res.data?.user
                                    isLoggedIn = true
                                    accessToken = res?.data?.accessToken.toString()
                                    saveSharedPreferences()
                                    Navigate(Router.HomeScreen)
                                }
                                Toast.makeText(
                                    context,
                                    res?.message ?: "Login Data Error",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            activeButton()
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<LoginResponse>>,
                            t: Throwable
                        ) {
                            Log.e("login-error", t.toString())
                            Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                                .show()
                            activeButton()
                        }
                    }
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun register(registerRequest: RegisterRequest, activeButton: () -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.register(registerRequest)!!
                    .enqueue(object : Callback<ResponseLayout<Any>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<Any>>,
                            response: Response<ResponseLayout<Any>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<Any>? = response.body()
                                Toast.makeText(
                                    context,
                                    res?.message ?: "Register Data Error",
                                    Toast.LENGTH_LONG
                                ).show()
                                if (res?.status == true) {
                                    Navigate(Router.LoginScreen)
                                }
                            }
                            activeButton()
                        }

                        override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                            Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                                .show()
                            activeButton()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout() {
        isLoggedIn = false;
        accessToken = "";
        userCurrent.value = null
        saveSharedPreferences()
        Toast.makeText(context, "Logged out!", Toast.LENGTH_LONG).show()
        Navigate(Router.HomeScreen)
    }

    fun report(report: ReportModel,activeButtonSubmit:()->Unit) {
        viewModelScope.launch {
            try {
                reportRepository.create(report)!!.enqueue(object : Callback<ResponseLayout<Any>> {
                    override fun onResponse(
                        call: Call<ResponseLayout<Any>>,
                        response: Response<ResponseLayout<Any>>
                    ) {
                        if (response.isSuccessful) {
                            val res: ResponseLayout<Any>? = response.body()
                            res?.message?.let {
                                NotificationService.ShowMessage.show(
                                    context,
                                    "Report notification",
                                    it
                                )
                            }
                            Navigate(Router.HomeScreen)
                        } else Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                            .show()
                        activeButtonSubmit()
                    }

                    override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                        Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                            .show()
                        activeButtonSubmit()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}