package com.example.endterm.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.endterm.ui.favorites.FavoritesViewModel

@Composable
fun FavoritesScreen(
    onOpenDetails: (Int) -> Unit = {},
    vm: FavoritesViewModel = hiltViewModel()
) {
    val items by vm.items.collectAsState()

    LazyColumn(contentPadding = PaddingValues(12.dp)) {
        items(items) { it ->
            Text(it.title, modifier = Modifier
                .clickable { onOpenDetails(it.id) }
                .padding(12.dp))
        }
    }
}