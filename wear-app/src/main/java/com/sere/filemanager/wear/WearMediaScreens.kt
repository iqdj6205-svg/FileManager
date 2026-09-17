package com.sere.filemanager.wear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.media.MediaItem
import com.sere.filemanager.core.ui.UiFormatters
import androidx.compose.runtime.Composable

@Composable
fun WearMediaLibraryScreen(
    title: String,
    items: List<MediaItem>,
    isLoading: Boolean,
    message: String?,
    onRefresh: () -> Unit,
    onOpen: (MediaItem) -> Unit,
    onBack: () -> Unit,
) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize().padding(10.dp), contentPadding = PaddingValues(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item { Text(title, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = onRefresh, modifier = Modifier.fillMaxWidth()) { Text(if (isLoading) "Loading" else "Refresh") } }
        message?.let { item { Text(it, textAlign = TextAlign.Center) } }
        if (!isLoading && items.isEmpty()) item { Text("No media visible", textAlign = TextAlign.Center) }
        items(items.size) { index ->
            val item = items[index]
            Chip(label = { Text(item.displayName, maxLines = 1, overflow = TextOverflow.Ellipsis) }, secondaryLabel = { Text(UiFormatters.compactBytes(item.sizeBytes)) }, onClick = { onOpen(item) }, modifier = Modifier.fillMaxWidth())
        }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
    }
}

@Composable
fun WearMediaPreviewScreen(item: MediaItem, onBack: () -> Unit) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize().padding(10.dp), contentPadding = PaddingValues(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item { Text("Preview", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
        item { Text(item.displayName, maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center) }
        item { Text(item.mimeType, textAlign = TextAlign.Center) }
        item { Text(UiFormatters.compactBytes(item.sizeBytes), textAlign = TextAlign.Center) }
        item { Text("Viewer/player controls will be optimized for round screens.", textAlign = TextAlign.Center) }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
    }
}
