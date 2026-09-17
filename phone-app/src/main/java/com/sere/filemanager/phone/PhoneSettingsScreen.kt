package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.model.AppSettings

@Composable
fun PhoneSettingsScreen(settings: AppSettings, onHidden: () -> Unit, onAdvanced: () -> Unit, onBattery: () -> Unit, onHaptics: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Settings")
        Toggle("Show hidden files", settings.showHiddenFiles, onHidden)
        Toggle("Advanced mode", settings.advancedMode, onAdvanced)
        Toggle("Battery saver", settings.batterySaver, onBattery)
        Toggle("Haptics", settings.hapticsEnabled, onHaptics)
        Text("Theme: ${settings.theme}")
        Button(onClick = onBack) { Text("Home") }
    }
}

@Composable private fun Toggle(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}
