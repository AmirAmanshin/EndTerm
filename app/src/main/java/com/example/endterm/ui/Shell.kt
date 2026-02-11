package com.example.endterm.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.endterm.ui.CommentsScreen

private data class BottomItem(val route: String, val label: String, val icon: @Composable () -> Unit)

@Composable
fun Shell() {
    val nav = rememberNavController()

    val items = listOf(
        BottomItem(Routes.FEED, "Feed") { Icon(Icons.Filled.Home, contentDescription = null) },
        BottomItem(Routes.SEARCH, "Search") { Icon(Icons.Filled.Search, contentDescription = null) },
        BottomItem(Routes.FAVORITES, "Favorites") { Icon(Icons.Filled.Favorite, contentDescription = null) }
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStack by nav.currentBackStackEntryAsState()
                val current = backStack?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        selected = current == item.route,
                        onClick = {
                            nav.navigate(item.route) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = item.icon,
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Routes.FEED,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.FEED) {
                FeedScreen(onOpenDetails = { id -> nav.navigate("details/$id") })
            }
            composable(Routes.SEARCH) {
                SearchScreen(onOpenDetails = { id -> nav.navigate("details/$id") })
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(onOpenDetails = { id -> nav.navigate("details/$id") })
            }

            composable(Routes.DETAILS) { backStack ->
                val id = backStack.arguments?.getString("id") ?: return@composable
                DetailsScreen(id = id, onOpenComments = { nav.navigate("comments/$id") })
            }

            composable(
                route = Routes.COMMENTS,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("id") ?: return@composable
                CommentsScreen(id = id)
            }
        }
    }
}