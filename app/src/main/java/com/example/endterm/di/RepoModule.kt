package com.example.endterm.di

import com.example.endterm.data.repo.GameRepositoryImpl
import com.example.endterm.domain.GameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds @Singleton
    abstract fun bindRepo(impl: GameRepositoryImpl): GameRepository
}