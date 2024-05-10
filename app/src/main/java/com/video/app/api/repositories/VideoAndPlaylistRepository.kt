package com.video.app.api.repositories

import com.video.app.api.ResponseLayout
import com.video.app.api.models.CreatePlaylistDto
import com.video.app.api.models.PlaylistModel
import com.video.app.api.models.VideoModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoAndPlaylistRepository {
    @Headers("Content-Type: application/json")
    @GET("playlists/my")
    fun myPlaylist(): Call<ResponseLayout<List<PlaylistModel>?>>

    @Headers("Content-Type: application/json")
    @POST("playlists/new")
    fun addPlaylist(@Body createPlaylistDto: CreatePlaylistDto): Call<ResponseLayout<PlaylistModel?>>

    @Headers("Content-Type: application/json")
    @GET("playlists/videos")
    fun getVideosPlaylist(
        @Query("playlistId") playlistId: Long
    ): Call<ResponseLayout<List<VideoModel>>>

    @Headers("Content-Type: application/json")
    @POST("playlists/add")
    fun addVideoToPlaylist(
        @Query("playlistId") playlistId: Long,
        @Query("videoId") videoId: Long
    ): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @DELETE("playlists/video")
    fun deleteVideoOfPlaylist(
        @Query("videoPlaylistId") videoPlaylistId: Long
    ): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @DELETE("playlists")
    fun deletePlaylist(@Query("id") id: Long): Call<ResponseLayout<Any>>

    @Headers("Content-Type: application/json")
    @GET("videos")
    fun getAllVideo(@Query("page") page: Int = 0): Call<ResponseLayout<List<VideoModel>>>

    @Headers("Content-Type: application/json")
    @GET("videos/search")
    fun getAllVideoWithCategoryId(
        @Query("page") page: Int = 0,
        @Query("category_id") categoryId: Long
    ): Call<ResponseLayout<List<VideoModel>>>

    @Headers("Content-Type: application/json")
    @GET("videos")
    fun getAllVideoWithUploaderId(
        @Query("page") page: Int = 0,
        @Query("uploader_id") uploaderId: Long
    ): Call<ResponseLayout<List<VideoModel>>>

    @Headers("Content-Type: application/json")
    @GET("videos/search")
    fun searchByNameLike(@Query("name") name: String): Call<ResponseLayout<List<VideoModel>>>

}