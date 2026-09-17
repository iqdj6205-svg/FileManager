package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RemoteSettingsScreen(
    settings: PhoneRemoteSettingsState,
    onUploads: () -> Unit,
    onDelete: () -> Unit,
    onAdvanced: () -> Unit,
    onHidden: () -> Unit,
    onSync: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Watch remote settings")
        SettingRow("Allow uploads", settings.allowUploads, onUploads)
        SettingRow("Allow delete/rename", settings.allowDelete, onDelete)
        SettingRow("Advanced mode", settings.advancedMode, onAdvanced)
        SettingRow("Show hidden files", settings.showHiddenFiles, onHidden)
        Text("Port: ${settings.port}, timeout: ${settings.autoStopMinutes} min")
        Button(onClick = onSync, modifier = Modifier.fillMaxWidth()) { Text("Sync to watch") }
        Button(onClick = onBack) { Text("Home") }
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    androidx.compose.foundation.layout.Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}
