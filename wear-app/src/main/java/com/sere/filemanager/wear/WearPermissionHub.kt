package com.sere.filemanager.wear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WearPermissionHub(
    state: WearPermissionHubState,
    onGrantMedia: () -> Unit,
    onGrantStorage: () -> Unit,
    onGrantNotifications: () -> Unit,
    onAdbGuide: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Permissions", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text("Missing: ${state.missingCount}")
        if (!state.storageGranted) Button(onClick = onGrantStorage, modifier = Modifier.fillMaxWidth()) { Text("Grant files") }
        if (!state.mediaGranted) Button(onClick = onGrantMedia, modifier = Modifier.fillMaxWidth()) { Text("Grant media") }
        if (!state.notificationsGranted) Button(onClick = onGrantNotifications, modifier = Modifier.fillMaxWidth()) { Text("Grant notifications") }
        Button(onClick = onAdbGuide, modifier = Modifier.fillMaxWidth()) { Text("ADB advanced") }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}

@Composable
fun WearAdbGuideScreen(onBack: () -> Unit) {
    androidx.wear.compose.foundation.lazy.ScalingLazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item { Text("ADB access") }
        AdbAccessGuide.commands.forEach { command -> item { Text(command) } }
        AdbAccessGuide.notes.forEach { note -> item { Text("• $note") } }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
    }
}
