package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.model.FileItem

@Composable
fun PhoneFileActionScreen(
    item: FileItem,
    onDetails: () -> Unit,
    onRename: () -> Unit,
    onCopy: () -> Unit,
    onMove: () -> Unit,
    onCopyHere: () -> Unit,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("File actions")
        Card(modifier = Modifier.fillMaxWidth()) { Column(modifier = Modifier.padding(16.dp)) { Text(item.name); Text(item.path) } }
        Button(onClick = onDetails, modifier = Modifier.fillMaxWidth()) { Text("Details") }
        Button(onClick = onRename, modifier = Modifier.fillMaxWidth()) { Text("Rename") }
        Button(onClick = onCopy, modifier = Modifier.fillMaxWidth()) { Text("Copy to…") }
        Button(onClick = onMove, modifier = Modifier.fillMaxWidth()) { Text("Move to…") }
        Button(onClick = onCopyHere, modifier = Modifier.fillMaxWidth()) { Text("Duplicate here") }
        Button(onClick = onShare, modifier = Modifier.fillMaxWidth()) { Text("Share") }
        Button(onClick = onFavorite, modifier = Modifier.fillMaxWidth()) { Text("Favorite") }
        Button(onClick = onDelete, modifier = Modifier.fillMaxWidth()) { Text("Delete") }
        Button(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun PhoneConfirmDeleteScreen(fileName: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Delete file?")
        Text(fileName)
        Text("This action cannot be undone.")
        Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth()) { Text("Delete") }
        Button(onClick = onCancel) { Text("Cancel") }
    }
}
