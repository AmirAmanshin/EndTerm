package com.example.endterm.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.endterm.domain.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchItem(
    val id: Int,
    val title: String,
    val thumbnailUrl: String
)

data class SearchUiState(
    val inputText: String = "",
    val isLoadingCatalog: Boolean = false,
    val items: List<SearchItem> = emptyList()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: GameRepository
) : ViewModel() {

    private val inputText = MutableStateFlow("")
    private val isLoadingCatalog = MutableStateFlow(false)
    private val results: StateFlow<List<SearchItem>> =
        inputText
            .debounce(400)
            .map { it.trim() }
            .distinctUntilChanged()
            .flatMapLatest { q ->
                repo.searchLocal(q).map { list ->
                    list.map { SearchItem(it.id, it.title, it.thumbnailUrl) }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val state: StateFlow<SearchUiState> =
        combine(inputText, isLoadingCatalog, results) { text, loading, items ->
            SearchUiState(
                inputText = text,
                isLoadingCatalog = loading,
                items = items
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchUiState())

    fun onInputChange(v: String) {
        inputText.value = v
    }

    fun ensureCatalogLoaded() {
        viewModelScope.launch {
            isLoadingCatalog.value = true
            runCatching { repo.refreshCatalog() }
            isLoadingCatalog.value = false
        }
    }
}