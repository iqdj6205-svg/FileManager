package com.sere.filemanager.wear

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text

@Composable
fun StorageAccessScreen(onMedia: () -> Unit, onAdvanced: () -> Unit, onBack: () -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 24.dp),
    ) {
        item { Text("Storage access", textAlign = TextAlign.Center) }
        item { Text("Use media permissions for normal files. Use ADB guide for protected locations.", textAlign = TextAlign.Center) }
        item { Chip(label = { Text("Media access") }, onClick = onMedia, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("ADB guide") }, onClick = onAdvanced, modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}
