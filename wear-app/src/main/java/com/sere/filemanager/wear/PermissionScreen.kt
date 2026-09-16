package com.sere.filemanager.wear

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionScreen(onRequestMedia: () -> Unit, onBack: () -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 24.dp),
    ) {
        item { Text("Permissions", textAlign = TextAlign.Center) }
        item { Text("Grant media access to browse images, videos, and audio.", textAlign = TextAlign.Center) }
        item { Chip(label = { Text("Grant media") }, onClick = onRequestMedia, modifier = Modifier.fillMaxWidth()) }
        item { Text("Full system memory requires ADB/root workflows and is not normal app access.", textAlign = TextAlign.Center) }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}
