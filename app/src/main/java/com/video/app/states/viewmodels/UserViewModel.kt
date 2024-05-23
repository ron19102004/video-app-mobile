package com.video.app.states.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.video.app.api.ResponseLayout
import com.video.app.api.RetrofitAPI
import com.video.app.api.models.ChangePasswordRequest
import com.video.app.api.models.InfoConfirmedLoggedInResponse
import com.video.app.api.models.InfoUserResponse
import com.video.app.api.models.LoginRequest
import com.video.app.api.models.LoginResponse
import com.video.app.api.models.RegisterRequest
import com.video.app.api.models.UserModel
import com.video.app.api.models.VIP
import com.video.app.api.models.VerifyOTPRequest
import com.video.app.api.repositories.UserRepository
import com.video.app.config.CONSTANT
import com.video.app.config.getFileFromUri
import com.video.app.ui.screens.Navigate
import com.video.app.ui.screens.Router
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SharedPreferencesAuthKey {
    const val ROOT = "auth"
    const val ACCESS_TOKEN = "access-token"
    const val IS_LOGGED_IN = "isLoggedIn"
    const val TFA = "two-factor-authentication"
}
@SuppressLint("StaticFieldLeak")
class UserViewModel : ViewModel() {
    private val userRepository by lazy {
        RetrofitAPI.service(CONSTANT.URL.PATH_URL_DEFAULT).create(UserRepository::class.java)
    }
    lateinit var context: Context;
    private lateinit var sharedPreferences: SharedPreferences;
    val userCurrent = MutableLiveData<UserModel?>(null);
    var isLoggedIn by mutableStateOf(false);
    var accessToken by mutableStateOf("");
    var tfa by mutableStateOf(false);
    var vip = MutableLiveData<VIP?>(null)

    //for your profile screen
    var infoUserConfirmed = MutableLiveData<UserModel?>(null)
    var isFetchingInfoUserConfirmed = mutableStateOf(false)
    var isSubscribing = mutableStateOf(false);

    private lateinit var toast: (String) -> Unit

    fun init(context: Context) {
        toast = {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
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
            .putBoolean(SharedPreferencesAuthKey.TFA, tfa)
            .apply()
    }

    fun changePassword(
        data: ChangePasswordRequest,
        action: () -> Unit = {},
        actionSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                userRepository.changePassword(data)!!
                    .enqueue(object : Callback<ResponseLayout<Any>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<Any>>,
                            response: Response<ResponseLayout<Any>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<Any>? = response.body()
                                if (res?.status == true) {
                                    toast("Change password successfully !!!")
                                    actionSuccess()
                                }
                            }
                            action()
                        }

                        override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                            toast("Change password failed !!!")
                            action()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                toast("Change password failed !!!")
                action()
            }
        }
    }

    fun updateAvatar(uri: Uri, action: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val file = getFileFromUri(context, uri)
                val requestBody = file?.asRequestBody("multipart/form-data".toMediaTypeOrNull());
                val imagePart =
                    MultipartBody.Part.createFormData("file", file?.name, requestBody!!)
                userRepository.updateAvatar(imagePart)!!
                    .enqueue(object : Callback<ResponseLayout<Any>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<Any>>,
                            response: Response<ResponseLayout<Any>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<Any>? = response.body()
                                if (res?.status == true) {
                                    Navigate(Router.MyProfileScreen)
                                }
                                toast(res?.message!!)
                            } else toast(response.message())
                            action()
                        }

                        override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                            t.printStackTrace()
                            toast("Error request")
                            action()
                        }
                    })

            } catch (e: Exception) {
                e.printStackTrace()
                toast("Error")
                Log.e("error-update-img", e.message.toString())
                action()
            }
        }
    }

    fun unsubscribe(id: Long, action: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                userRepository.unsubscribe(id)!!.enqueue(object : Callback<ResponseLayout<Any>> {
                    override fun onResponse(
                        call: Call<ResponseLayout<Any>>,
                        response: Response<ResponseLayout<Any>>
                    ) {
                        if (response.isSuccessful) {
                            val res: ResponseLayout<Any>? = response?.body()
                            if (res?.status == true) {
                                isSubscribing.value = false
                            } else {
                                toast("Subscribe has an error !!!")
                            }
                        }
                        action()
                    }

                    override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                        t.printStackTrace()
                        toast("Subscribe has an error !!!")
                        action()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                toast("Subscribe has an error !!!")
                action()
            }
        }
    }

    fun subscribe(id: Long, action: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                userRepository.subscribe(id)!!.enqueue(object : Callback<ResponseLayout<Any>> {
                    override fun onResponse(
                        call: Call<ResponseLayout<Any>>,
                        response: Response<ResponseLayout<Any>>
                    ) {
                        if (response.isSuccessful) {
                            val res: ResponseLayout<Any>? = response?.body()
                            if (res?.status == true) {
                                isSubscribing.value = true
                            } else {
                                toast("Subscribe has an error !!!")
                            }
                        }
                        action()
                    }

                    override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                        action()
                        t.printStackTrace()
                        toast("Subscribe has an error !!!")
                    }
                })
            } catch (e: Exception) {
                action()
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun fetchInfoUserConfirmed(id: Long, action: () -> Unit = {}) {
        isFetchingInfoUserConfirmed.value = true
        viewModelScope.launch {
            try {
                userRepository.getInfoUserConfirmed(id)!!
                    .enqueue(object : Callback<ResponseLayout<UserModel?>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<UserModel?>>,
                            response: Response<ResponseLayout<UserModel?>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<UserModel?>? = response.body()
                                infoUserConfirmed.value = res?.data
                            }
                            action()
                            isFetchingInfoUserConfirmed.value = false
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<UserModel?>>,
                            t: Throwable
                        ) {
                            t.printStackTrace()
                            action()
                            isFetchingInfoUserConfirmed.value = false
                        }

                    })
            } catch (e: Exception) {
                e.printStackTrace()
                action()
                isFetchingInfoUserConfirmed.value = false
            }
        }
    }

    fun fetchInfoUserConfirmedWhenLoggedIn(id: Long, action: () -> Unit = {}) {
        isFetchingInfoUserConfirmed.value = true
        viewModelScope.launch {
            try {
                userRepository.getInfoUserConfirmedWhenLoggedIn(id)!!
                    .enqueue(object : Callback<ResponseLayout<InfoConfirmedLoggedInResponse?>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<InfoConfirmedLoggedInResponse?>>,
                            response: Response<ResponseLayout<InfoConfirmedLoggedInResponse?>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<InfoConfirmedLoggedInResponse?>? =
                                    response.body()
                                infoUserConfirmed.value = res?.data?.user
                                isSubscribing.value = res?.data?.isSubscribing == true
                            }
                            action()
                            isFetchingInfoUserConfirmed.value = false
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<InfoConfirmedLoggedInResponse?>>,
                            t: Throwable
                        ) {
                            t.printStackTrace()
                            action()
                            isFetchingInfoUserConfirmed.value = false
                        }

                    })
            } catch (e: Exception) {
                e.printStackTrace()
                action()
                isFetchingInfoUserConfirmed.value = false
            }
        }
    }

    fun loadUserFormToken(action: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                userRepository.info()!!
                    .enqueue(object : Callback<ResponseLayout<InfoUserResponse>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<InfoUserResponse>>,
                            response: Response<ResponseLayout<InfoUserResponse>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<InfoUserResponse>? = response.body()
                                userCurrent.value = res?.data?.user
                                vip.value = res?.data?.vip
                            } else logout()
                            action()
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<InfoUserResponse>>,
                            t: Throwable
                        ) {
                            Log.e("loadUserFormToken-error", t.message.toString())
                            toast("Subscribe has an error !!!")
                            action()
                        }

                    })
            } catch (e: Exception) {
                e.printStackTrace()
                action()
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
                                    val user: UserModel? = res.data?.user;
                                    val token: String = res?.data?.token.toString();
                                    tfa = res?.data?.tfa == true
                                    if (tfa) {
                                        Navigate(
                                            Router.OTPScreen(
                                                user?.username.toString(),
                                                token
                                            )
                                        )
                                    } else {
                                        userCurrent.value = user
                                        vip.value = res?.data?.vip
                                        isLoggedIn = true
                                        accessToken = token
                                        saveSharedPreferences()
                                        Navigate(Router.HomeScreen)
                                    }
                                }
                            }
                            Toast.makeText(
                                context,
                                response.body()?.message ?: "Error",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            activeButton()
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<LoginResponse>>,
                            t: Throwable
                        ) {
                            Log.e("login-error", t.toString())
                            toast("Subscribe has an error !!!")
                            activeButton()
                        }
                    }
                    )
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
                activeButton()

            }
        }
    }

    fun verifyOTP(verifyOTPRequest: VerifyOTPRequest, activeButton: () -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.verifyOTP(verifyOTPRequest)!!
                    .enqueue(object : Callback<ResponseLayout<LoginResponse?>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<LoginResponse?>>,
                            response: Response<ResponseLayout<LoginResponse?>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<LoginResponse?>? = response.body()
                                if (res?.status == true) {
                                    userCurrent.value = res?.data?.user
                                    vip.value = res?.data?.vip
                                    isLoggedIn = true
                                    accessToken = res?.data?.token.toString()
                                    saveSharedPreferences()
                                    Navigate(Router.HomeScreen)
                                }
                            }
                            toast(response.body()?.message ?: "Error")
                            activeButton()
                            Log.e("verify-otp", response.toString())
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<LoginResponse?>>,
                            t: Throwable
                        ) {
                            toast("An error has occurred!")
                            activeButton()
                            Log.e("verify-otp-error", t.toString())
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
                activeButton()
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
                            } else Toast.makeText(context, response.message(), Toast.LENGTH_LONG)
                                .show()
                            activeButton()
                        }

                        override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                            toast("An error has occurred!")
                            activeButton()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                activeButton()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
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

    fun changeTFA(value: Boolean, activeButton: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                userRepository.changeTFA()!!.enqueue(object : Callback<ResponseLayout<Any>> {
                    override fun onResponse(
                        call: Call<ResponseLayout<Any>>,
                        response: Response<ResponseLayout<Any>>
                    ) {
                        if (response.isSuccessful) {
                            val res: ResponseLayout<Any>? = response.body();
                            if (res?.status == true) {
                                tfa = value
                            }
                            Toast.makeText(context, res?.message, Toast.LENGTH_LONG)
                                .show()
                        } else Toast.makeText(context, response.message(), Toast.LENGTH_LONG)
                            .show()
                        activeButton()
                    }

                    override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                        toast("An error has occurred!")
                        activeButton()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
                activeButton()
            }
        }
    }

    fun sendOTP(token: String, activeButtonSubmit: () -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.sendOTP(token)!!.enqueue(object : Callback<ResponseLayout<Any>> {
                    override fun onResponse(
                        call: Call<ResponseLayout<Any>>,
                        response: Response<ResponseLayout<Any>>
                    ) {
                        if (response.isSuccessful) {
                            val res: ResponseLayout<Any>? = response.body()
                            if (res?.status == false) {
                                Navigate(Router.LoginScreen)
                            }
                            Toast.makeText(context, res?.message, Toast.LENGTH_LONG)
                                .show()
                        } else Toast.makeText(context, response.message(), Toast.LENGTH_LONG)
                            .show()
                        activeButtonSubmit()
                        Log.e("send-otp", response.toString())
                    }

                    override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                        toast("An error has occurred!")
                        activeButtonSubmit()
                        Log.e("send-otp-error", t.toString())
                    }
                })
            } catch (e: Exception) {
                Log.e("error-send-otp", e.toString())
                activeButtonSubmit()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun registerVIP(month: Int, activeButton: () -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.registerVIP(month)!!
                    .enqueue(object : Callback<ResponseLayout<VIP?>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<VIP?>>,
                            response: Response<ResponseLayout<VIP?>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<VIP?>? = response.body()
                                if (res?.status == true) {
                                    vip.value = res?.data
                                    Navigate(Router.HomeScreen)
                                }
                            }
                            Toast.makeText(
                                context,
                                response.body()?.message ?: "Error",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            activeButton()
                        }

                        override fun onFailure(call: Call<ResponseLayout<VIP?>>, t: Throwable) {
                            toast("An error has occurred!")
                            activeButton()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
                activeButton()
            }
        }
    }

    fun cancelVIP() {
        viewModelScope.launch {
            try {
                userRepository.cancelVIP()!!.enqueue(object : Callback<ResponseLayout<Any>> {
                    override fun onResponse(
                        call: Call<ResponseLayout<Any>>,
                        response: Response<ResponseLayout<Any>>
                    ) {
                        if (response.isSuccessful) {
                            val res: ResponseLayout<Any>? = response.body()
                            if (res?.status == true) {
                                vip.value = null
                            }
                            Toast.makeText(context, res?.message, Toast.LENGTH_LONG)
                                .show()
                        } else Toast.makeText(context, response.message(), Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                        toast("An error has occurred!")
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}