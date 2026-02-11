package com.example.endterm.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.endterm.domain.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class FeedUiState(
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val items: List<FeedItem> = emptyList()
)

data class FeedItem(
    val id: Int,
    val title: String,
    val thumbnailUrl: String,
    val description: String
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repo: GameRepository
) : ViewModel() {

    private val _refreshing = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<FeedUiState> =
        combine(repo.observeFeed(), _refreshing, _error) { games, refreshing, err ->
            FeedUiState(
                isRefreshing = refreshing,
                error = err,
                items = games.map {
                    FeedItem(it.id, it.title, it.thumbnailUrl, it.shortDescription)
                }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeedUiState(isRefreshing = true))

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            _error.value = null
            runCatching { repo.refreshFeed() }
                .onFailure { _error.value = it.message ?: "Network error" }
            _refreshing.value = false
        }
    }
}