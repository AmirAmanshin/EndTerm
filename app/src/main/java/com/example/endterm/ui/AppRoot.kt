package com.example.endterm.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument


object Routes {
    const val LOGIN = "login"
    const val FEED = "feed"
    const val SEARCH = "search"
    const val DETAILS = "details/{id}"
    const val FAVORITES = "favorites"
    const val COMMENTS = "comments/{id}"
}

@Composable
fun AppRoot() {
    val nav = rememberNavController()
    val sessionVm: com.example.endterm.ui.session.SessionViewModel = hiltViewModel()
    val user by sessionVm.user.collectAsState()

    val start = if (user == null) Routes.LOGIN else Routes.FEED

    NavHost(navController = nav, startDestination = start, modifier = Modifier.fillMaxSize()) {
        composable(Routes.LOGIN) { LoginScreen() }
        composable(Routes.FEED) { FeedScreen(onOpenDetails = { id -> nav.navigate("details/$id") }) }
        composable(Routes.SEARCH) { SearchScreen(onOpenDetails = { id -> nav.navigate("details/$id") }) }
        composable(Routes.FAVORITES) { FavoritesScreen(onOpenDetails = { id -> nav.navigate("details/$id") }) }
        composable(Routes.DETAILS) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            DetailsScreen(id = id, onOpenComments = { nav.navigate("comments/$id") })
        }
        composable(
            route = Routes.COMMENTS,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("id")
                ?: error("Game id is missing in route")

            CommentsScreen(id = id)
        }
    }

    LaunchedEffect(user) {
        if (user == null) nav.navigate(Routes.LOGIN) {
            popUpTo(0) { inclusive = true }
        } else {
            nav.navigate(Routes.FEED) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}