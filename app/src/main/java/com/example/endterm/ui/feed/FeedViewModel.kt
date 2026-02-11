package com.example.endterm.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.endterm.data.api.FreeToGameApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedUiState(
    val isLoading: Boolean = false,
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
    private val api: FreeToGameApi
) : ViewModel() {

    private val _state = MutableStateFlow(FeedUiState())
    val state: StateFlow<FeedUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching { api.games(sortBy = "popularity") }
                .onSuccess { list ->
                    _state.value = FeedUiState(
                        isLoading = false,
                        items = list.take(40).map {
                            FeedItem(
                                id = it.id,
                                title = it.title,
                                thumbnailUrl = it.thumbnailUrl,
                                description = it.shortDescription
                            )
                        }
                    )
                }
                .onFailure { e ->
                    _state.value = FeedUiState(isLoading = false, error = e.message ?: "Error")
                }
        }
    }
}