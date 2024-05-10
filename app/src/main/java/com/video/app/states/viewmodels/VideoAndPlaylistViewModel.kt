package com.video.app.states.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.video.app.api.ResponseLayout
import com.video.app.api.RetrofitAPI
import com.video.app.api.URL
import com.video.app.api.models.CreatePlaylistDto
import com.video.app.api.models.PlaylistModel
import com.video.app.api.models.VideoModel
import com.video.app.api.repositories.VideoAndPlaylistRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoAndPlaylistViewModel : ViewModel() {
    private val videoAndPlaylistRepository by lazy {
        RetrofitAPI.service(URL.path).create(VideoAndPlaylistRepository::class.java)
    }
    private lateinit var context: Context

    //for home screen
    var videosOnHomeScreen = MutableLiveData<List<VideoModel>?>(emptyList())
    var isFetchingVideosOnHome = mutableStateOf(false)

    //for video player screen
    var videosWithUploaderId = MutableLiveData<List<VideoModel>?>(emptyList())
    var isErrorFetchVideoWithUploaderId = mutableStateOf(false)
    var isLoadingVideosWithUploaderId = mutableStateOf(false)

    //for search screen
    var queryOnSearchScreen = mutableStateOf("")
    var videosOnSearchScreen = MutableLiveData<List<VideoModel>?>(emptyList())
    var isLoadingSearchVideo = mutableStateOf(false)

    //for playlist
    var myPlaylist = MutableLiveData<List<PlaylistModel>?>(emptyList())
    var videosOfPlaylistId = MutableLiveData<List<VideoModel>?>(emptyList())
    var isFetchingVideosPlaylist = mutableStateOf(false)

    fun init(context: Context, userViewModel: UserViewModel) {
        this.context = context
        if (userViewModel.isLoggedIn) {
            loadMyPlaylist()
        }
    }

    fun searchVideosByName(name: String, action: () -> Unit = {}) {
        isLoadingSearchVideo.value = true
        videosOnSearchScreen.value = emptyList()
        viewModelScope.launch {
            try {
                videoAndPlaylistRepository.searchByNameLike(name)!!
                    .enqueue(object : Callback<ResponseLayout<List<VideoModel>>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            response: Response<ResponseLayout<List<VideoModel>>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<List<VideoModel>>? = response.body()
                                if (res?.status == true) {
                                    videosOnSearchScreen.value = res?.data
                                }
                            } else Toast.makeText(
                                context,
                                "An error has occurred!",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            isLoadingSearchVideo.value = false
                            Log.e("search-data-videos", videosOnSearchScreen.value.toString())
                            action()
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            t: Throwable
                        ) {
                            Log.e("search-videos-error", t.toString())
                            isLoadingSearchVideo.value = false
                            action()
                            Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                isLoadingSearchVideo.value = false
                action()
                Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun fetchVideosHome(action: () -> Unit = {}) {
        isFetchingVideosOnHome.value = true
        viewModelScope.launch {
            delay(1000L)
            try {
                videoAndPlaylistRepository.getAllVideo(page = 0)!!
                    .enqueue(object : Callback<ResponseLayout<List<VideoModel>>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            response: Response<ResponseLayout<List<VideoModel>>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<List<VideoModel>>? = response.body()
                                if (res?.status == true) {
                                    videosOnHomeScreen.value = res?.data
                                }
                            }
                            Log.e("data-videos", videosOnHomeScreen.value.toString())
                            action()
                            isFetchingVideosOnHome.value = false
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            t: Throwable
                        ) {
                            Log.e("data-videos-error", t.toString())
                            action()
                            isFetchingVideosOnHome.value = false
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                action()
                isFetchingVideosOnHome.value = false
            }
        }
    }

    fun fetchVideosHomeWithCategoryId(action: () -> Unit = {}, categoryId: Long) {
        isFetchingVideosOnHome.value = true
        viewModelScope.launch {
            delay(1000L)
            try {
                videoAndPlaylistRepository.getAllVideoWithCategoryId(
                    page = 0,
                    categoryId = categoryId
                )!!
                    .enqueue(object : Callback<ResponseLayout<List<VideoModel>>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            response: Response<ResponseLayout<List<VideoModel>>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<List<VideoModel>>? = response.body()
                                if (res?.status == true) {
                                    videosOnHomeScreen.value = res?.data
                                }
                            }
                            Log.e(
                                "data-videos-with-category-id",
                                videosOnHomeScreen.value.toString()
                            )
                            action()
                            isFetchingVideosOnHome.value = false
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            t: Throwable
                        ) {
                            Log.e("data-videos-error", t.toString())
                            action()
                            isFetchingVideosOnHome.value = false
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                action()
                isFetchingVideosOnHome.value = false
            }
        }
    }

    fun fetchVideosWithUploaderId(action: () -> Unit = {}, uploaderId: Long) {
        isLoadingVideosWithUploaderId.value = true
        viewModelScope.launch {
            delay(1000L)
            try {
                videoAndPlaylistRepository.getAllVideoWithUploaderId(
                    page = 0,
                    uploaderId = uploaderId
                )!!
                    .enqueue(object : Callback<ResponseLayout<List<VideoModel>>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            response: Response<ResponseLayout<List<VideoModel>>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<List<VideoModel>>? = response.body()
                                if (res?.status == true) {
                                    videosWithUploaderId.value = res?.data
                                }
                            } else {
                                isErrorFetchVideoWithUploaderId.value = true
                            }
                            Log.e("data-videos", videosWithUploaderId.value.toString())
                            action()
                            isLoadingVideosWithUploaderId.value = false
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            t: Throwable
                        ) {
                            Log.e("data-videos-error", t.toString())
                            action()
                            isErrorFetchVideoWithUploaderId.value = true
                            isLoadingVideosWithUploaderId.value = false

                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                isErrorFetchVideoWithUploaderId.value = true
                isLoadingVideosWithUploaderId.value = false
                action()
            }
        }
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
                Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun addVideoToPlaylist(action: () -> Unit = {}, videoId: Long, playlistId: Long) {
        viewModelScope.launch {
            try {
                videoAndPlaylistRepository.addVideoToPlaylist(playlistId, videoId)!!
                    .enqueue(object : Callback<ResponseLayout<Any>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<Any>>,
                            response: Response<ResponseLayout<Any>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<Any>? = response?.body()
                                Toast.makeText(context, res?.message ?: "Error", Toast.LENGTH_LONG)
                                    .show()
                            }
                            action()
                        }

                        override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                            Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                                .show()
                            action()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                    .show()
                action()
            }
        }
    }

    fun deleteVideoOfPlaylist(action: () -> Unit = {}, videoPlaylistId: Long) {
        viewModelScope.launch {
            try {
                videoAndPlaylistRepository.deleteVideoOfPlaylist(videoPlaylistId)!!
                    .enqueue(object : Callback<ResponseLayout<Any>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<Any>>,
                            response: Response<ResponseLayout<Any>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<Any>? = response?.body()
                                Toast.makeText(context, res?.message ?: "Error", Toast.LENGTH_LONG)
                                    .show()
                            }
                            action()
                        }

                        override fun onFailure(call: Call<ResponseLayout<Any>>, t: Throwable) {
                            Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                                .show()
                            action()
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                    .show()
                action()
            }
        }
    }

    fun getVideosPlaylist(action: () -> Unit = {}, playlistId: Long) {
        isFetchingVideosPlaylist.value = true
        viewModelScope.launch {
            try {
                delay(1000L)
                videoAndPlaylistRepository.getVideosPlaylist(playlistId)!!
                    .enqueue(object : Callback<ResponseLayout<List<VideoModel>>> {
                        override fun onResponse(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            response: Response<ResponseLayout<List<VideoModel>>>
                        ) {
                            if (response.isSuccessful) {
                                val res: ResponseLayout<List<VideoModel>>? = response?.body()
                                if (res?.status == true) {
                                    videosOfPlaylistId.value = res?.data
                                }
                                Log.e("video-playlists", res.toString())
                            }
                            action()
                            isFetchingVideosPlaylist.value = false
                        }

                        override fun onFailure(
                            call: Call<ResponseLayout<List<VideoModel>>>,
                            t: Throwable
                        ) {
                            Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                                .show()
                            action()
                            isFetchingVideosPlaylist.value = false
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "An error has occurred!", Toast.LENGTH_LONG)
                    .show()
                action()
                isFetchingVideosPlaylist.value = false
            }
        }
    }
}