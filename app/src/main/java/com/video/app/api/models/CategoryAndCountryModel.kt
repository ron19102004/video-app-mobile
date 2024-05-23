package com.video.app.api.models

class CategoryModel(
    val id: Long,
    val name: String,
    val slug: String,
    val image: String?=null,
)

class CountryModel(
    val id: Long,
    val name: String,
    val slug: String
)

