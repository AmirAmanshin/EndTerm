package com.example.endterm.data.mappers


import com.example.endterm.data.api.GameDto
import com.example.endterm.domain.Game

fun GameDto.toDomain(): Game = Game(
    id = id,
    title = title,
    thumbnailUrl = thumbnailUrl,
    shortDescription = shortDescription,
    genre = genre,
    platform = platform
)