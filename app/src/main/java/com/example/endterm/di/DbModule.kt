package com.example.endterm.di

import android.content.Context
import androidx.room.Room
import com.example.endterm.data.db.AppDatabase
import com.example.endterm.data.db.FavoriteDao
import com.example.endterm.data.db.GameDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides @Singleton
    fun db(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db").build()

    @Provides fun gameDao(db: AppDatabase): GameDao = db.gameDao()
    @Provides fun favoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
}