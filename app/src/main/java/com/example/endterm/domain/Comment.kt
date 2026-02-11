package com.example.endterm.domain


data class Comment(
    val id: String,
    val uid: String,
    val text: String,
    val createdAt: Long
)