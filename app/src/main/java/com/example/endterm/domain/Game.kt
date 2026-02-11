package com.example.endterm.domain

data class Game(
    val id: Int,
    val title: String,
    val thumbnailUrl: String,
    val shortDescription: String,
    val genre: String?,
    val platform: String?
)