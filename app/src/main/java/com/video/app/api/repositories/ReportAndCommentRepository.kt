package com.video.app.api.repositories

import com.video.app.api.ResponseLayout
import com.video.app.api.models.CommentModel
import com.video.app.api.models.CreateCommentDto
import com.video.app.api.models.ReportModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReportAndCommentRepository {
    @POST("reports/new")
    fun create(@Body report: ReportModel): Call<ResponseLayout<Any>>

    @POST("comments/new")
    fun createComment(@Body data: CreateCommentDto): Call<ResponseLayout<Any>>

    @GET("comments")
    fun getCommentsByVideoId(@Query("videoId") videoId: Long): Call<ResponseLayout<List<CommentModel>?>>
}