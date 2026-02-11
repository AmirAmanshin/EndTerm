package com.example.endterm.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.endterm.data.firebase.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    repo: AuthRepository
) : ViewModel() {
    val user: StateFlow<FirebaseUser?> =
        repo.observeUser().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}