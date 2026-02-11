package com.example.endterm.data.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<GameEntity>)

    @Query("SELECT * FROM games ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<GameEntity?>

    @Query("DELETE FROM games")
    suspend fun clearGames()
}