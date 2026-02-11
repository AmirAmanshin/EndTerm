package com.example.endterm.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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

    NavHost(navController = nav, startDestination = Routes.SEARCH) {
//        composable(Routes.LOGIN) { LoginScreen() }
        composable(Routes.FEED) { FeedScreen(onOpenDetails = { id -> nav.navigate("details/$id") }) }
        composable(Routes.SEARCH) { SearchScreen(onOpenDetails = { id -> nav.navigate("details/$id") }) }
        composable(Routes.FAVORITES) { FavoritesScreen(onOpenDetails = { id -> nav.navigate("details/$id") }) }
        composable(Routes.DETAILS) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            DetailsScreen(id = id, onOpenComments = { nav.navigate("comments/$id") })
        }
        composable(Routes.COMMENTS) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
//            CommentsScreen(id = id)
        }
    }
}