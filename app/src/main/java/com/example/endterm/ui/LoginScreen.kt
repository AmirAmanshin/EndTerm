package com.example.endterm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.endterm.ui.login.LoginViewModel

@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmail,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = state.pass,
            onValueChange = vm::onPass,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") }
        )

        if (state.error != null) Text("Error: ${state.error}")

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = vm::signIn, enabled = !state.isLoading) { Text("Sign in") }
            OutlinedButton(onClick = vm::signUp, enabled = !state.isLoading) { Text("Sign up") }
        }

        if (state.isLoading) CircularProgressIndicator()
    }
}