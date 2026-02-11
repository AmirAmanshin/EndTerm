package com.example.endterm.data.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(fav: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE gameId = :gameId")
    suspend fun remove(gameId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE gameId = :gameId)")
    fun observeIsFavorite(gameId: Int): Flow<Boolean>

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun observeAllFavorites(): Flow<List<FavoriteEntity>>
}