package com.example.endterm.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.endterm.ui.search.SearchViewModel

@Composable
fun SearchScreen(
    onOpenDetails: (Int) -> Unit = {},
    vm: SearchViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.ensureCatalogLoaded()
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        OutlinedTextField(
            value = state.inputText,
            onValueChange = vm::onInputChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search game title") }
        )

        if (state.isLoadingCatalog) {
            Spacer(Modifier.height(12.dp))
            Row {
                CircularProgressIndicator()
                Spacer(Modifier.width(12.dp))
                Text("Updating catalog...")
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.items) { item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onOpenDetails(item.id) }
                ) {
                    AsyncImage(
                        model = item.thumbnailUrl,
                        contentDescription = item.title,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(item.title)
                }
            }
        }
    }
}