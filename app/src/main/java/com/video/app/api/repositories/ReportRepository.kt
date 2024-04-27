package com.video.app.api.repositories

import com.video.app.api.ResponseLayout
import com.video.app.api.models.ReportModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportRepository {
    @POST("/reports/new")
    fun create(@Body report:ReportModel):Call<ResponseLayout<Any>>
}