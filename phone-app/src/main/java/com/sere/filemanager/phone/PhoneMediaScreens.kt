package com.sere.filemanager.phone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.media.MediaItem
import com.sere.filemanager.core.ui.UiFormatters

@Composable
fun PhoneMediaLibraryScreen(state: PhoneMediaState, onRefresh: () -> Unit, onRequestAccess: () -> Unit, onOpen: (MediaItem) -> Unit, onBack: () -> Unit) {
    var selectedBucket by remember { mutableStateOf<String?>(null) }
    val visibleItems = remember(state.items, selectedBucket) { selectedBucket?.let { bucket -> state.items.filter { it.bucketName == bucket } } ?: state.items }
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Phone media", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Button(onClick = onRefresh) { Text(if (state.isLoading) "Loading…" else "Refresh") }; Button(onClick = onRequestAccess) { Text("Grant access") }; Button(onClick = onBack) { Text("Home") } }
        Text("Grant access lets FileManager read Android media collections. It does not grant protected/system folders.")
        state.message?.let { Text(it) }
        Text("${visibleItems.size} shown · ${state.items.size} total · ${state.buckets.size} buckets")
        if (state.buckets.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(selected = selectedBucket == null, onClick = { selectedBucket = null }, label = { Text("All ${state.items.size}") }) }
                items(state.buckets) { bucket -> FilterChip(selected = selectedBucket == bucket.name, onClick = { selectedBucket = bucket.name }, label = { Text("${bucket.name} (${bucket.itemCount})", maxLines = 1, overflow = TextOverflow.Ellipsis) }) }
            }
        }
        if (!state.isLoading && visibleItems.isEmpty()) Text(if (selectedBucket == null) "No media visible. Grant media access or refresh after adding files." else "No media in selected bucket.")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(visibleItems) { item -> PhoneMediaRow(item, onOpen) } }
    }
}

@Composable private fun PhoneMediaRow(item: MediaItem, onOpen: (MediaItem) -> Unit) { Card(modifier = Modifier.fillMaxWidth().clickable { onOpen(item) }) { Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) { Text(mediaKind(item)); Column(modifier = Modifier.weight(1f)) { Text(item.displayName, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(mediaMeta(item), maxLines = 1, overflow = TextOverflow.Ellipsis); item.bucketName?.let { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) } } } } }

private fun mediaKind(item: MediaItem): String = when {
    item.mimeType?.startsWith("image") == true -> "IMG"
    item.mimeType?.startsWith("video") == true -> "VID"
    item.mimeType?.startsWith("audio") == true -> "AUD"
    else -> "MEDIA"
}

private fun mediaMeta(item: MediaItem): String {
    val parts = buildList {
        item.mimeType?.let { add(it) }
        item.sizeBytes?.let { add(UiFormatters.compactBytes(it)) }
        item.durationMillis?.let { add("${it / 1000}s") }
    }
    return if (parts.isEmpty()) "No metadata" else parts.joinToString(" · ")
}

@Composable
fun PhoneMediaPreviewScreen(item: MediaItem, onPlay: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Media preview", style = MaterialTheme.typography.headlineSmall)
        Card(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Text(mediaPreviewLabel(item), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            }
        }
        Text(item.displayName, maxLines = 2, overflow = TextOverflow.Ellipsis)
        item.bucketName?.let { Text("Bucket: $it") }
        Text("MIME: ${item.mimeType ?: "unknown"}")
        Text("Size: ${UiFormatters.compactBytes(item.sizeBytes)}")
        item.durationMillis?.let { Text("Duration: ${it / 1000}s") }
        Text("URI: ${item.uri}", maxLines = 4, overflow = TextOverflow.Ellipsis)
        Button(onClick = onPlay, modifier = Modifier.fillMaxWidth(), enabled = item.mimeType?.startsWith("audio") == true || item.mimeType?.startsWith("video") == true) { Text(if (item.mimeType?.startsWith("audio") == true || item.mimeType?.startsWith("video") == true) "Open player" else "Player unavailable") }
        Button(onClick = onBack) { Text("Back") }
    }
}

private fun mediaPreviewLabel(item: MediaItem): String = when {
    item.mimeType?.startsWith("video") == true -> "VID\nVideo preview"
    item.mimeType?.startsWith("audio") == true -> "AUD\nAudio track"
    item.mimeType?.startsWith("image") == true -> "IMG\nOpen image preview"
    else -> "MEDIA\nPreview"
}
