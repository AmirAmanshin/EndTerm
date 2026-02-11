package com.example.endterm.ui.favorites

import androidx.lifecycle.ViewModel
import com.example.endterm.data.firebase.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesActionsViewModel @Inject constructor(
    private val auth: AuthRepository
) : ViewModel() {
    fun logout() = auth.signOut()
}