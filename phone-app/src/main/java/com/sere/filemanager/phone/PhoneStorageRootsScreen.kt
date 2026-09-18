package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.files.StorageRoot
import com.sere.filemanager.core.files.StorageRootResolver
import com.sere.filemanager.core.files.StorageRootState
import com.sere.filemanager.phone.ui.PhoneActionRow
import com.sere.filemanager.phone.ui.PhoneScreenScaffold
import com.sere.filemanager.phone.ui.PhoneSectionCard

@Composable
fun PhoneStorageRootsScreen(state: StorageRootState, onSelect: (StorageRoot) -> Unit, onAddTree: () -> Unit, onBack: () -> Unit) {
    val resolver = StorageRootResolver()
    PhoneScreenScaffold(
        title = "Storage roots",
        subtitle = "Choose where the phone file browser starts.",
    ) {
        PhoneActionRow(primary = "Add folder access", onPrimary = onAddTree, secondary = "Home", onSecondary = onBack)
        PhoneSectionCard("Status") {
            Text(state.message ?: "Select a built-in folder or add Android folder access.")
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.roots) { root ->
                Card(onClick = { onSelect(root) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(if (root.id == state.selectedRootId) "Selected: ${root.title}" else root.title, style = MaterialTheme.typography.titleMedium)
                        Text(resolver.preferredDisplayPath(root))
                        if (root.uri != null) Text("SAF permission saved", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
