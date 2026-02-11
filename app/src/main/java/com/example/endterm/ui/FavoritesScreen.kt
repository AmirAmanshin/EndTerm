package com.example.endterm.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable fun LoginScreen() { Text("Login") }
@Composable fun SearchScreen() { Text("Search") }
@Composable fun FavoritesScreen() { Text("Favorites") }
@Composable fun DetailsScreen(id: String, onOpenComments: () -> Unit) { Text("Details: $id") }
@Composable fun CommentsScreen(id: String) { Text("Comments for: $id") }