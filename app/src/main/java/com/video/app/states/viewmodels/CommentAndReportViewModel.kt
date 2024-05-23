package com.video.app.states.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.video.app.api.ResponseLayout
import com.video.app.api.RetrofitAPI
import com.video.app.api.URL
import com.video.app.api.models.CommentModel
import com.video.app.api.models.CreateCommentDto
import com.video.app.api.models.ReportModel
import com.video.app.api.repositories.ReportAndCommentRepository
import com.video.app.services.NotificationService
import com.video.app.ui.screens.Navigate
import com.video.app.ui.screens.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class CommentAndReportViewModel : ViewModel() {
    private val reportAndCommentRepository by lazy {
        RetrofitAPI.service(URL.path).create(ReportAndCommentRepository::class.java)
    }
    lateinit var context: Context;
    private lateinit var toast: (String) -> Unit

    var comments = MutableLiveData<List<CommentModel>?>(emptyList())
    fun init(context: Context) {
        toast = {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        this.context = context;
    }

    fun getCommentsByVideoId(videoId: Long, action: () -> Unit={}) {
        comments.value = emptyList()
        viewModelScope.launch {
            delay(500L)
            try {
                reportAndCommentRepository.getCommentsByVideoId(videoId)
                    .enqueue(object : Callback<ResponseLayout<List<CommentModel>?>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<List<CommentModel>?>>,
                            response: Response<ResponseLayout<List<CommentModel>?>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<List<CommentModel>?>? = response.body()
                                comments.value = res?.data
                            }
                            Log.e("Comments", response.body().toString())
                            action()
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<List<CommentModel>?>>,
                            t: Throwable
                        ) {
                            t.printStackTrace()
                            action()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                action()
            }
        }
    }

    fun addComment(
        createCommentDto: CreateCommentDto,
        action: () -> Unit = {},
        success: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                reportAndCommentRepository.createComment(createCommentDto)
                    .enqueue(object : Callback<ResponseLayout<Any>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<Any>>,
                            response: Response<ResponseLayout<Any>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<Any>? = response.body()
                                if (res?.status == true) success()
                                toast(res?.message ?: "")
                            }
                            action()
                        }

                        override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                            toast("An error has occurred!")
                            action()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                action()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun report(report: ReportModel, activeButtonSubmit: () -> Unit) {
        viewModelScope.launch {
            try {
                reportAndCommentRepository.create(report)
                    .enqueue(object : Callback<ResponseLayout<Any>> {
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
                            } else toast("An error has occurred!")
                            activeButtonSubmit()
                        }

                        override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                            toast("An error has occurred!")
                            activeButtonSubmit()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                activeButtonSubmit()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}