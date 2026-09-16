package com.sere.filemanager.wear

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.model.FileItem

@Composable
fun FileActionSheet(
    item: FileItem,
    onDetails: () -> Unit,
    onRename: () -> Unit,
    onCopy: () -> Unit,
    onMove: () -> Unit,
    onDelete: () -> Unit,
    onFavorite: () -> Unit,
    onBack: () -> Unit,
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 22.dp),
    ) {
        item { Text(item.name, maxLines = 2, textAlign = TextAlign.Center) }
        item { Chip(label = { Text("Details") }, onClick = onDetails, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Rename") }, onClick = onRename, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Copy") }, onClick = onCopy, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Move") }, onClick = onMove, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Favorite") }, onClick = onFavorite, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Delete") }, onClick = onDelete, modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}

@Composable
fun FileDetailsScreen(item: FileItem, size: String, onBack: () -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 22.dp),
    ) {
        item { Text("Details", textAlign = TextAlign.Center) }
        item { Text(item.name, maxLines = 2, textAlign = TextAlign.Center) }
        item { Text(item.type.name) }
        item { Text(size.ifBlank { "No size" }) }
        item { Text(item.path, maxLines = 4, textAlign = TextAlign.Center) }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}

@Composable
fun ConfirmDeleteScreen(fileName: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 26.dp),
    ) {
        item { Text("Delete?", textAlign = TextAlign.Center) }
        item { Text(fileName, maxLines = 2, textAlign = TextAlign.Center) }
        item { Text("This cannot be undone.", textAlign = TextAlign.Center) }
        item { Chip(label = { Text("Delete") }, onClick = onConfirm, modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = onCancel) { Text("Cancel") } }
    }
}
