package com.sere.filemanager.wear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.files.StorageRoot
import com.sere.filemanager.core.files.StorageRootState

@Composable
fun WearStorageRootsScreen(state: StorageRootState, onSelect: (StorageRoot) -> Unit, onBack: () -> Unit) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize().padding(10.dp), contentPadding = PaddingValues(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item { Text("Storage", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
        state.roots.forEach { root -> item { Chip(label = { Text(if (root.id == state.selectedRootId) "✓ ${root.title}" else root.title) }, secondaryLabel = { Text(root.path ?: root.type.name) }, onClick = { onSelect(root) }, modifier = Modifier.fillMaxWidth()) } }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
    }
}
