package com.example.endterm.data.firebase

import com.example.endterm.domain.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentsRepository @Inject constructor(
    private val db: FirebaseDatabase,
    private val auth: FirebaseAuth
) {
    private fun ref(gameId: Int): DatabaseReference =
        db.reference.child("comments").child(gameId.toString())

    fun observeComments(gameId: Int): Flow<List<Comment>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { node ->
                    val id = node.key ?: return@mapNotNull null
                    val uid = node.child("uid").getValue(String::class.java) ?: return@mapNotNull null
                    val text = node.child("text").getValue(String::class.java) ?: return@mapNotNull null
                    val createdAt = node.child("createdAt").getValue(Long::class.java) ?: 0L
                    Comment(id = id, uid = uid, text = text, createdAt = createdAt)
                }.sortedBy { it.createdAt }
                trySend(list).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref(gameId).addValueEventListener(listener)
        awaitClose { ref(gameId).removeEventListener(listener) }
    }

    suspend fun add(gameId: Int, text: String) {
        val uid = auth.currentUser?.uid ?: error("Not logged in")
        val node = ref(gameId).push()
        val data = mapOf(
            "uid" to uid,
            "text" to text.trim(),
            "createdAt" to ServerValue.TIMESTAMP
        )
        node.setValue(data).await()
    }

    suspend fun delete(gameId: Int, commentId: String) {
        ref(gameId).child(commentId).removeValue().await()
    }

    fun myUid(): String? = auth.currentUser?.uid
}