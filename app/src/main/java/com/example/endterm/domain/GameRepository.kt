package com.example.endterm.domain

import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun observeFeed(): Flow<List<Game>>
    fun observeGame(id: Int): Flow<Game?>
    suspend fun refreshFeed()
    suspend fun toggleFavorite(gameId: Int)
    fun observeIsFavorite(gameId: Int): Flow<Boolean>
    fun observeFavorites(): Flow<List<Game>>
}