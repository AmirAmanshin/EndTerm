package com.example.endterm.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.endterm.data.firebase.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onEmail(v: String) { _state.value = _state.value.copy(email = v) }
    fun onPass(v: String) { _state.value = _state.value.copy(pass = v) }

    fun signIn() = auth { repo.signIn(_state.value.email, _state.value.pass) }
    fun signUp() = auth { repo.signUp(_state.value.email, _state.value.pass) }

    private fun auth(block: suspend () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching { block() }
                .onFailure { _state.value = _state.value.copy(error = it.message ?: "Auth error") }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}