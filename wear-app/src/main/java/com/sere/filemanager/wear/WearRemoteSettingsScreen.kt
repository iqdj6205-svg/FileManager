package com.sere.filemanager.wear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text

@Composable
fun WearRemoteSettingsScreen(
    state: WearRemoteSettingsUiState,
    onToggleUploads: () -> Unit,
    onToggleDelete: () -> Unit,
    onTogglePin: () -> Unit,
    onBack: () -> Unit,
) {
    androidx.wear.compose.foundation.lazy.ScalingLazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item { Text("Remote settings") }
        item { WearSettingRow("PIN", state.requirePin, onTogglePin) }
        item { WearSettingRow("Uploads", state.allowUploads, onToggleUploads) }
        item { WearSettingRow("Delete/rename", state.allowDelete, onToggleDelete) }
        item { Text("Port ${state.port}") }
        item { Text("Auto stop ${state.autoStopMinutes} min") }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
    }
}

@Composable
private fun WearSettingRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}

data class WearRemoteSettingsUiState(
    val port: Int = 8080,
    val requirePin: Boolean = true,
    val allowUploads: Boolean = false,
    val allowDelete: Boolean = false,
    val autoStopMinutes: Int = 15,
)
