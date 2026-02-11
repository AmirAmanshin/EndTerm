package com.example.endterm.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameDto(
    val id: Int,
    val title: String,
    @Json(name = "thumbnail") val thumbnailUrl: String,
    @Json(name = "short_description") val shortDescription: String,
    val genre: String?,
    val platform: String?
)