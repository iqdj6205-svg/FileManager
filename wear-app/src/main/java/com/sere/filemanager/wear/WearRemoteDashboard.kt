package com.sere.filemanager.wear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.model.RemoteSession
import com.sere.filemanager.core.remote.RemoteServerStatusStore

@Composable
fun WearRemoteDashboard(
    session: RemoteSession = RemoteServerStatusStore.current(),
    networkLabel: String,
    batteryPercent: Int?,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onSettings: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Text("Remote", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text("State: ${session.state}")
        Text("Network: $networkLabel")
        if (batteryPercent != null) Text("Battery: $batteryPercent%")
        session.url?.let { Text("URL: $it") }
        session.pin?.let { Text("PIN: $it") }
        if (session.url == null) Text("Connect watch to Wi‑Fi/Bluetooth network and start server.")
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) { Text("Start") }
        Button(onClick = onStop, modifier = Modifier.fillMaxWidth()) { Text("Stop") }
        Button(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Settings") }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
