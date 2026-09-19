package com.sere.filemanager.phone

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.phone.ui.PhoneScreenScaffold
import com.sere.filemanager.phone.ui.PhoneSectionCard

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
    PhoneScreenScaffold(title = "File actions", subtitle = item.name) {
        PhoneSectionCard { Text(item.path) }
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
    PhoneScreenScaffold(title = "Delete file?", subtitle = fileName) {
        PhoneSectionCard { Text("This action cannot be undone.") }
        Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth()) { Text("Delete") }
        Button(onClick = onCancel) { Text("Cancel") }
    }
}
