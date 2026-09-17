package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.files.StorageRoot
import com.sere.filemanager.core.files.StorageRootState

@Composable
fun PhoneStorageRootsScreen(state: StorageRootState, onSelect: (StorageRoot) -> Unit, onAddTree: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Storage roots")
        state.message?.let { Text(it) }
        Button(onClick = onAddTree, modifier = Modifier.fillMaxWidth()) { Text("Add folder access") }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(state.roots) { root ->
                Card(onClick = { onSelect(root) }, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(if (root.id == state.selectedRootId) "✓ ${root.title}" else root.title)
                        Text(root.path ?: root.uri ?: root.type.name)
                    }
                }
            }
        }
        Button(onClick = onBack) { Text("Home") }
    }
}
