package com.sere.filemanager.phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.sere.filemanager.core.media.ImagePreviewState

@Composable
fun PhoneImagePreviewScreen(state: ImagePreviewState, onZoom: () -> Unit, onRotateLeft: () -> Unit, onRotateRight: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(state.title ?: "Image", style = MaterialTheme.typography.headlineSmall)
        Card(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (state.uri.isNullOrBlank()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No image selected") }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(state.uri.toUri()),
                    contentDescription = state.title,
                    contentScale = if (state.isZoomed) ContentScale.Crop else ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().clipToBounds().rotate(state.rotationDegrees),
                )
            }
        }
        state.message?.let { Text(it) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onZoom) { Text(if (state.isZoomed) "Fit" else "Zoom") }
            Button(onClick = onRotateLeft) { Text("Left") }
            Button(onClick = onRotateRight) { Text("Right") }
        }
        Button(onClick = onBack) { Text("Back") }
    }
}
