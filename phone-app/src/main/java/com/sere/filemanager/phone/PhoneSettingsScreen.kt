package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.model.AppSettings
import com.sere.filemanager.core.model.AppThemeMode
import com.sere.filemanager.phone.ui.PhoneActionRow
import com.sere.filemanager.phone.ui.PhoneScreenScaffold
import com.sere.filemanager.phone.ui.PhoneSectionCard

@Composable
fun PhoneSettingsScreen(
    settings: AppSettings,
    onHidden: () -> Unit,
    onAdvanced: () -> Unit,
    onBattery: () -> Unit,
    onHaptics: () -> Unit,
    onTheme: (AppThemeMode) -> Unit,
    onBack: () -> Unit,
) {
    PhoneScreenScaffold(
        title = "Settings",
        subtitle = "Shared app preferences for file browsing, power use, and UI.",
    ) {
        PhoneSectionCard("File manager") {
            Toggle("Show hidden files", settings.showHiddenFiles, onHidden)
            Toggle("Advanced mode", settings.advancedMode, onAdvanced)
        }
        PhoneSectionCard("Device behavior") {
            Toggle("Battery saver", settings.batterySaver, onBattery)
            Toggle("Haptics", settings.hapticsEnabled, onHaptics)
        }
        PhoneSectionCard("Theme") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppThemeMode.entries.forEach { mode ->
                    FilterChip(selected = settings.theme == mode, onClick = { onTheme(mode) }, label = { Text(mode.name) })
                }
            }
        }
        PhoneActionRow(primary = "Home", onPrimary = onBack)
    }
}

@Composable private fun Toggle(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}
