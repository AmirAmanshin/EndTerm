package com.example.endterm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.endterm.ui.details.DetailsViewModel

@Composable
fun DetailsScreen(
    id: String,
    onOpenComments: () -> Unit,
    vm: DetailsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val g = state.game

    if (g == null) {
        Text("Loading from cache...")
        return
    }

    Column(Modifier.padding(16.dp)) {
        Text(g.title)
        Spacer(Modifier.height(12.dp))
        AsyncImage(model = g.thumbnailUrl, contentDescription = g.title, modifier = Modifier.size(180.dp))
        Spacer(Modifier.height(12.dp))
        Text(g.shortDescription)
        Spacer(Modifier.height(12.dp))
        Text("Genre: ${g.genre ?: "-"}")
        Text("Platform: ${g.platform ?: "-"}")
        Spacer(Modifier.height(16.dp))

        Button(onClick = { vm.toggleFavorite(state.isFavorite) }) {
            Text(if (state.isFavorite) "Remove from favorites" else "Add to favorites")
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onOpenComments) { Text("Open comments") }
    }
}