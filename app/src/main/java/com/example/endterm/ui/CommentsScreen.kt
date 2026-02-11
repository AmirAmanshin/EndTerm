package com.example.endterm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.endterm.ui.comments.CommentsViewModel

@Composable
fun CommentsScreen(
    id: String, // оставляем для route, но VM берёт id из SavedStateHandle
    vm: CommentsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Comments", style = MaterialTheme.typography.headlineSmall)
        if (state.error != null) Text("Error: ${state.error}")

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.items) { c ->
                Card {
                    Column(Modifier.padding(12.dp)) {
                        Text(c.text)
                        Spacer(Modifier.height(6.dp))
                        if (state.myUid != null && c.uid == state.myUid) {
                            TextButton(onClick = { vm.delete(c.id) }) { Text("Delete") }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = state.input,
                onValueChange = vm::onInput,
                modifier = Modifier.weight(1f),
                label = { Text("Write a comment") }
            )
            Button(onClick = vm::send) { Text("Send") }
        }
    }
}