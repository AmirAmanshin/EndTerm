package com.example.endterm.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.endterm.ui.feed.FeedViewModel

@Composable
fun FeedScreen(
    onOpenDetails: (Int) -> Unit,
    vm: FeedViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            Column(Modifier.padding(16.dp)) {
                Text("Error: ${state.error}")
                Spacer(Modifier.height(8.dp))
                Text("Tap to retry", modifier = Modifier.clickable { vm.load() })
            }
        }
        else -> {
            LazyColumn(contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.items) { item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onOpenDetails(item.id) }
                    ) {
                        AsyncImage(
                            model = item.thumbnailUrl,
                            contentDescription = item.title,
                            modifier = Modifier.size(96.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(item.title)
                            Spacer(Modifier.height(6.dp))
                            Text(item.description)
                        }
                    }
                }
            }
        }
    }
}