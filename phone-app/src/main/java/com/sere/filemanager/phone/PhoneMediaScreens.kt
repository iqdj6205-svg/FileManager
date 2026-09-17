package com.sere.filemanager.phone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.media.MediaItem
import com.sere.filemanager.core.ui.UiFormatters

@Composable
fun PhoneMediaLibraryScreen(state: PhoneMediaState, onRefresh: () -> Unit, onOpen: (MediaItem) -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Phone media", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onRefresh) { Text(if (state.isLoading) "Loading…" else "Refresh") }
            Button(onClick = onBack) { Text("Home") }
        }
        state.message?.let { Text(it) }
        Text("${state.items.size} media files")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.items) { item -> PhoneMediaRow(item, onOpen) }
        }
    }
}

@Composable
private fun PhoneMediaRow(item: MediaItem, onOpen: (MediaItem) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onOpen(item) }) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(if (item.mimeType.startsWith("image")) "🖼" else if (item.mimeType.startsWith("video")) "🎬" else "🎵")
            Column(modifier = Modifier.weight(1f)) {
                Text(item.displayName, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${item.mimeType} · ${UiFormatters.compactBytes(item.sizeBytes)}")
            }
        }
    }
}

@Composable
fun PhoneMediaPreviewScreen(item: MediaItem, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Media preview", style = MaterialTheme.typography.headlineSmall)
        Text(item.displayName, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text("MIME: ${item.mimeType}")
        Text("Size: ${UiFormatters.compactBytes(item.sizeBytes)}")
        Text("URI: ${item.uri}", maxLines = 4, overflow = TextOverflow.Ellipsis)
        Text("Image viewer and Media3 playback controls will be wired here next.")
        Button(onClick = onBack) { Text("Back") }
    }
}
