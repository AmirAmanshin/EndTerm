package com.example.endterm.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun observeUser(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser).isSuccess }
        auth.addAuthStateListener(listener)
        trySend(auth.currentUser).isSuccess
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signIn(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email.trim(), pass).await()
    }

    suspend fun signUp(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email.trim(), pass).await()
    }

    fun signOut() = auth.signOut()
}