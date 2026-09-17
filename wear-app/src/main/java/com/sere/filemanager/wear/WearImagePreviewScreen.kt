package com.sere.filemanager.wear

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import coil.compose.rememberAsyncImagePainter
import com.sere.filemanager.core.media.ImagePreviewState

@Composable
fun WearImagePreviewScreen(state: ImagePreviewState, onZoom: () -> Unit, onRotateLeft: () -> Unit, onRotateRight: () -> Unit, onBack: () -> Unit) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize().padding(10.dp), contentPadding = PaddingValues(vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item { Text(state.title ?: "Image", maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
        item {
            Image(
                painter = rememberAsyncImagePainter(state.uri?.toUri() ?: Uri.EMPTY),
                contentDescription = state.title,
                contentScale = if (state.isZoomed) ContentScale.Crop else ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp).rotate(state.rotationDegrees),
            )
        }
        state.message?.let { item { Text(it, textAlign = TextAlign.Center) } }
        item { Chip(label = { Text(if (state.isZoomed) "Fit" else "Zoom") }, onClick = onZoom, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Rotate left") }, onClick = onRotateLeft, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Rotate right") }, onClick = onRotateRight, modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
    }
}
