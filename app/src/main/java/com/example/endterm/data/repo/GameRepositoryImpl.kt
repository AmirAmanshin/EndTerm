package com.example.endterm.data.repo

import com.example.endterm.data.api.FreeToGameApi
import com.example.endterm.data.db.FavoriteDao
import com.example.endterm.data.db.FavoriteEntity
import com.example.endterm.data.db.GameDao
import com.example.endterm.data.db.GameEntity
import com.example.endterm.domain.Game
import com.example.endterm.domain.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class GameRepositoryImpl @Inject constructor(
    private val api: FreeToGameApi,
    private val gameDao: GameDao,
    private val favoriteDao: FavoriteDao
) : GameRepository {

    private val staleMs = 6.hours.inWholeMilliseconds

    override fun observeFeed(): Flow<List<Game>> =
        gameDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeGame(id: Int): Flow<Game?> =
        gameDao.observeById(id).map { it?.toDomain() }

    override suspend fun refreshFeed() {
        val now = System.currentTimeMillis()
        val remote = api.games(sortBy = "popularity")
        val entities = remote.take(80).map {
            GameEntity(
                id = it.id,
                title = it.title,
                thumbnailUrl = it.thumbnailUrl,
                shortDescription = it.shortDescription,
                genre = it.genre,
                platform = it.platform,
                updatedAt = now
            )
        }
        gameDao.upsertAll(entities)
    }

    override suspend fun toggleFavorite(gameId: Int) {
        // простой toggle: если есть — удалить, иначе добавить
        // (проверку делаем запросом в БД)
        // Room не даёт sync-return Flow, поэтому делаем EXISTS через query + firstOrNull в usecase/VM
        // Здесь проще: пытаемся удалить, если удалилось 0 строк — добавим.
        // Но у нас DAO remove без return, сделаем вариант через observeIsFavorite на VM.
        // Оставим реальную реализацию в VM (ниже).
        throw NotImplementedError("Use toggleFavorite(gameId, isFavorite) overload in VM")
    }

    suspend fun setFavorite(gameId: Int, value: Boolean) {
        if (value) favoriteDao.add(FavoriteEntity(gameId = gameId, addedAt = System.currentTimeMillis()))
        else favoriteDao.remove(gameId)
    }

    override fun observeIsFavorite(gameId: Int): Flow<Boolean> =
        favoriteDao.observeIsFavorite(gameId)

    override fun observeFavorites(): Flow<List<Game>> =
        combine(
            favoriteDao.observeAllFavorites(),
            gameDao.observeAll()
        ) { favs, games ->
            val favIds = favs.map { it.gameId }.toSet()
            games.filter { favIds.contains(it.id) }.map { it.toDomain() }
        }

    override suspend fun refreshCatalog() {
        val now = System.currentTimeMillis()
        val remote = api.games(platform = "all", sortBy = "popularity") // базовый каталог
        val entities = remote.map {
            GameEntity(
                id = it.id,
                title = it.title,
                thumbnailUrl = it.thumbnailUrl,
                shortDescription = it.shortDescription,
                genre = it.genre,
                platform = it.platform,
                updatedAt = now
            )
        }
        gameDao.upsertAll(entities)
    }

    override fun searchLocal(query: String): Flow<List<Game>> {
        val q = query.trim().lowercase()
        return gameDao.observeAll().map { list ->
            if (q.isBlank()) emptyList()
            else list
                .filter { it.title.lowercase().contains(q) }
                .map { it.toDomain() }
                .take(50)
        }
    }
}

private fun GameEntity.toDomain(): Game = Game(
    id = id,
    title = title,
    thumbnailUrl = thumbnailUrl,
    shortDescription = shortDescription,
    genre = genre,
    platform = platform
)