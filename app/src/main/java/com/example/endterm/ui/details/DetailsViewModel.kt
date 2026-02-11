package com.example.endterm.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.endterm.data.repo.GameRepositoryImpl
import com.example.endterm.domain.Game
import com.example.endterm.domain.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailsUiState(
    val game: Game? = null,
    val isFavorite: Boolean = false
)

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: GameRepository,
    private val impl: GameRepositoryImpl
) : ViewModel() {

    private val gameId: Int = checkNotNull(savedStateHandle["id"]).toString().toInt()

    val state: StateFlow<DetailsUiState> =
        combine(
            repo.observeGame(gameId),
            repo.observeIsFavorite(gameId)
        ) { game, fav ->
            DetailsUiState(game = game, isFavorite = fav)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailsUiState())

    fun toggleFavorite(current: Boolean) {
        viewModelScope.launch { impl.setFavorite(gameId, !current) }
    }
}