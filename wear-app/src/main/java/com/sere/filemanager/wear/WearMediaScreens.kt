package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.media.MediaItem
import com.sere.filemanager.core.ui.UiFormatters
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearEmpty
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearLoading
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearTitle

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
    WearRotaryList {
        wearTitle(title, "${items.size} items")
        wearPrimaryAction(if (isLoading) "Loading" else "Refresh", onRefresh)
        wearInfo(message)
        wearLoading(isLoading)
        wearEmpty(!isLoading && items.isEmpty(), "No media visible")
        items(items.size) { index ->
            val item = items[index]
            wearPrimaryAction(mediaRowLabel(item)) { onOpen(item) }
            item.bucketName?.let { bucket -> wearInfo(bucket) }
        }
        wearBackAction(onBack)
    }
}

private fun mediaRowLabel(item: MediaItem): String {
    val name = item.displayName.take(28)
    val size = UiFormatters.compactBytes(item.sizeBytes)
    val duration = item.durationMillis?.let { " · ${it / 1000}s" }.orEmpty()
    return "${mediaKind(item)} $name · $size$duration"
}

private fun mediaKind(item: MediaItem): String = when {
    item.mimeType?.startsWith("image") == true -> "IMG"
    item.mimeType?.startsWith("video") == true -> "VID"
    item.mimeType?.startsWith("audio") == true -> "AUD"
    else -> "MEDIA"
}

@Composable
fun WearMediaPreviewScreen(item: MediaItem, onPlay: () -> Unit, onBack: () -> Unit) {
    WearRotaryList {
        wearTitle("Preview", item.displayName)
        wearInfo(item.bucketName)
        wearInfo(item.mimeType ?: "Unknown MIME")
        wearInfo(UiFormatters.compactBytes(item.sizeBytes))
        item.durationMillis?.let { wearInfo("${it / 1000}s") }
        wearPrimaryAction("Open player", onPlay)
        wearBackAction(onBack)
    }
}
