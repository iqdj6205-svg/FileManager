package com.sere.filemanager.wear

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.sere.filemanager.core.media.ImagePreviewState
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearSecondaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun WearImagePreviewScreen(
    state: ImagePreviewState,
    onZoom: () -> Unit,
    onRotateLeft: () -> Unit,
    onRotateRight: () -> Unit,
    onBack: () -> Unit,
) {
    WearRotaryList {
        wearTitle(state.title ?: "Image")
        val uri = state.uri
        if (uri.isNullOrBlank()) {
            wearInfo("No image selected")
        } else {
            item {
                Image(
                    painter = rememberAsyncImagePainter(uri.toUri()),
                    contentDescription = state.title,
                    contentScale = if (state.isZoomed) ContentScale.Crop else ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp).clipToBounds().rotate(state.rotationDegrees),
                )
            }
        }
        wearInfo(state.message)
        wearPrimaryAction(if (state.isZoomed) "Fit" else "Zoom", onZoom)
        wearSecondaryAction("Rotate left", onRotateLeft)
        wearSecondaryAction("Rotate right", onRotateRight)
        wearBackAction(onBack)
    }
}
