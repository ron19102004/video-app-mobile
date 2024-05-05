package com.video.app.api.repositories

import com.video.app.api.ResponseLayout
import com.video.app.api.models.CreatePlaylistDto
import com.video.app.api.models.PlaylistModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoAndPlaylistRepository {
    @GET("playlists/my")
    fun myPlaylist(): Call<ResponseLayout<List<PlaylistModel>?>>

    @POST("playlists/new")
    fun addPlaylist(@Body createPlaylistDto: CreatePlaylistDto): Call<ResponseLayout<PlaylistModel?>>

    @DELETE("playlists")
    fun deletePlaylist(@Query("id") id: Long): Call<ResponseLayout<Any>>
}