package com.video.app.api.repositories

import com.video.app.api.models.CategoryModel
import com.video.app.api.models.CountryModel
import retrofit2.Call
import retrofit2.http.GET

interface CategoryAndCountryRepository {
    @GET("/categories")
    fun findAllCategory(): Call<List<CategoryModel>>
    @GET("/countries")
    fun findAllCountry():Call<List<CountryModel>>
}