package com.example.endterm.ui

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.endterm.ui.session.SessionViewModel

@Composable
fun AppRoot() {
    val nav = rememberNavController()
    val sessionVm: SessionViewModel = hiltViewModel()
    val user by sessionVm.user.collectAsState()

    val start = if (user == null) Routes.LOGIN else "shell"

    NavHost(navController = nav, startDestination = start) {
        composable(Routes.LOGIN) { LoginScreen() }
        composable("shell") { Shell() }
    }

    LaunchedEffect(user) {
        if (user == null) {
            nav.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
        } else {
            nav.navigate("shell") { popUpTo(0) { inclusive = true } }
        }
    }
}