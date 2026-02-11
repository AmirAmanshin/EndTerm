package com.example.endterm.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides @Singleton fun auth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides @Singleton fun rtdb(): FirebaseDatabase =
        FirebaseDatabase.getInstance().apply { setPersistenceEnabled(true) }
}