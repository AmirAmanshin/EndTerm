package com.example.endterm.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val thumbnailUrl: String,
    val shortDescription: String,
    val genre: String?,
    val platform: String?,
    val updatedAt: Long
)