package com.example.endterm.ui.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.endterm.data.firebase.CommentsRepository
import com.example.endterm.domain.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommentsUiState(
    val input: String = "",
    val items: List<Comment> = emptyList(),
    val error: String? = null,
    val myUid: String? = null
)

@HiltViewModel
class CommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: CommentsRepository
) : ViewModel() {

    private val gameId: Int = checkNotNull(savedStateHandle["id"]).toString().toInt()

    private val input = MutableStateFlow("")
    private val error = MutableStateFlow<String?>(null)

    val state: StateFlow<CommentsUiState> =
        combine(
            input,
            repo.observeComments(gameId),
            error
        ) { text, list, err ->
            CommentsUiState(
                input = text,
                items = list,
                error = err,
                myUid = repo.myUid()
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CommentsUiState(myUid = repo.myUid()))

    fun onInput(v: String) { input.value = v }

    fun send() {
        val t = input.value.trim()
        if (t.isBlank()) return
        viewModelScope.launch {
            error.value = null
            runCatching { repo.add(gameId, t) }
                .onFailure { error.value = it.message ?: "Send failed" }
            input.value = ""
        }
    }

    fun delete(commentId: String) {
        viewModelScope.launch {
            error.value = null
            runCatching { repo.delete(gameId, commentId) }
                .onFailure { error.value = it.message ?: "Delete failed" }
        }
    }
}