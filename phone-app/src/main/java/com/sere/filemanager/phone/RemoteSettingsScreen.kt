package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun RemoteSettingsScreen(
    settings: PhoneRemoteSettingsState,
    onUploads: () -> Unit,
    onDelete: () -> Unit,
    onRequirePin: () -> Unit,
    onLocalOnly: () -> Unit,
    onPort: (String) -> Unit,
    onAutoStop: (String) -> Unit,
    onAdvanced: () -> Unit,
    onHidden: () -> Unit,
    onSync: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Watch remote settings")
        SettingRow("Require PIN", settings.requirePin, onRequirePin)
        SettingRow("Local network only", settings.localNetworkOnly, onLocalOnly)
        SettingRow("Allow uploads", settings.allowUploads, onUploads)
        SettingRow("Allow delete/rename", settings.allowDelete, onDelete)
        SettingRow("Advanced mode", settings.advancedMode, onAdvanced)
        SettingRow("Show hidden files", settings.showHiddenFiles, onHidden)
        OutlinedTextField(value = settings.port.toString(), onValueChange = onPort, label = { Text("Port 1024-65535") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = settings.autoStopMinutes.toString(), onValueChange = onAutoStop, label = { Text("Auto stop minutes 1-60") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Text("These values are sent to the watch with Sync to watch.")
        Button(onClick = onSync, modifier = Modifier.fillMaxWidth()) { Text("Sync to watch") }
        Button(onClick = onBack) { Text("Home") }
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}
