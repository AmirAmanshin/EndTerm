package com.example.endterm.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.endterm.ui.favorites.FavoritesActionsViewModel
import com.example.endterm.ui.favorites.FavoritesViewModel
import com.example.endterm.data.firebase.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun FavoritesScreen(
    onOpenDetails: (Int) -> Unit = {},
    vm: FavoritesViewModel = hiltViewModel(),
    actionsVm: FavoritesActionsViewModel = hiltViewModel()
) {
    val items by vm.items.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Favorites", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = { actionsVm.logout() }) { Text("Logout") }
        }

        androidx.compose.foundation.lazy.LazyColumn(
            contentPadding = PaddingValues(12.dp)
        ) {
            items(items) { it ->
                Text(
                    it.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable { onOpenDetails(it.id) }
                )
            }
        }
    }
}