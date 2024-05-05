package com.video.app.states.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.video.app.api.ResponseLayout
import com.video.app.api.RetrofitAPI
import com.video.app.api.URL
import com.video.app.api.models.CreatePlaylistDto
import com.video.app.api.models.PlaylistModel
import com.video.app.api.repositories.VideoAndPlaylistRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoAndPlaylistViewModel : ViewModel() {
    private val videoAndPlaylistRepository by lazy {
        RetrofitAPI.service(URL.path).create(VideoAndPlaylistRepository::class.java)
    }
    private lateinit var context: Context
    var myPlaylist = MutableLiveData<List<PlaylistModel>?>(emptyList())
    fun init(context: Context) {
        this.context = context
        loadMyPlaylist()
    }

    fun loadMyPlaylist() {
        viewModelScope.launch {
            try {
                videoAndPlaylistRepository.myPlaylist()!!
                    .enqueue(object : Callback<ResponseLayout<List<PlaylistModel>?>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<List<PlaylistModel>?>>,
                            response: Response<ResponseLayout<List<PlaylistModel>?>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<List<PlaylistModel>?>? = response.body()
                                if (res?.status == true) {
                                    myPlaylist.value = res.data
                                }
                                Log.e("data-playlist", res.toString())
                            }
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<List<PlaylistModel>?>>,
                            t: Throwable
                        ) {
                            Log.e("error-playlist", t.toString())
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addPlaylist(createPlaylistDto: CreatePlaylistDto, action: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                videoAndPlaylistRepository.addPlaylist(createPlaylistDto)!!
                    .enqueue(object : Callback<ResponseLayout<PlaylistModel?>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<PlaylistModel?>>,
                            response: Response<ResponseLayout<PlaylistModel?>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<PlaylistModel?>? = response.body()
                                if (res?.status == true) {
                                    loadMyPlaylist()
                                }
                                Toast.makeText(context, res?.message ?: "Error", Toast.LENGTH_LONG)
                                    .show()
                            } else Toast.makeText(context, response?.message(), Toast.LENGTH_LONG)
                                .show()
                            action()
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<PlaylistModel?>>,
                            t: Throwable
                        ) {
                            Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                                .show()
                            action()
                        }
                    })
            } catch (e: Exception) {
                action()
            }
        }
    }

    fun deletePlaylist(id: Long, action: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                videoAndPlaylistRepository.deletePlaylist(id)!!
                    .enqueue(object : Callback<ResponseLayout<Any>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<Any>>,
                            response: Response<ResponseLayout<Any>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<Any>? = response.body()
                                if (res?.status == true) {
                                    loadMyPlaylist()
                                }
                                Toast.makeText(context, res?.message ?: "Error", Toast.LENGTH_LONG)
                                    .show()
                            } else Toast.makeText(
                                context,
                                response?.body()?.message ?: response?.message(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            action()
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<Any>>,
                            t: Throwable
                        ) {
                            Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                                .show()
                            action()
                        }
                    })
            } catch (e: Exception) {
                action()
            }
        }
    }
}