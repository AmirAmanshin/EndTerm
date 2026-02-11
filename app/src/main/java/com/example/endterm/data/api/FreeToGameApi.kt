package com.example.endterm.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface FreeToGameApi {
    @GET("games")
    suspend fun games(
        @Query("platform") platform: String? = null,
        @Query("category") category: String? = null,
        @Query("sort-by") sortBy: String? = null
    ): List<GameDto>

    @GET("game")
    suspend fun gameDetails(@Query("id") id: Int): Map<String, Any> // временно, на этапе 2 сделаем норм DTO
}