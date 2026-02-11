package com.example.endterm.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.endterm.domain.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class FavoritesItem(val id: Int, val title: String)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    repo: GameRepository
) : ViewModel() {
    val items: StateFlow<List<FavoritesItem>> =
        repo.observeFavorites()
            .map { list -> list.map { FavoritesItem(it.id, it.title) } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}