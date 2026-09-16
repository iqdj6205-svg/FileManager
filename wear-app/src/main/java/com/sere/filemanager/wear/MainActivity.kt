package com.sere.filemanager.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import com.sere.filemanager.core.model.RemoteServerState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WearFileManagerApp() }
    }
}

private enum class WearScreen { Home, Files, Media, Remote, Settings, Advanced }

@Composable
fun WearFileManagerApp(viewModel: FileManagerViewModel = viewModel()) {
    val screen = remember { mutableStateOf(WearScreen.Home) }
    val state by viewModel.state.collectAsState()
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            when (screen.value) {
                WearScreen.Home -> HomeScreen(onOpen = { screen.value = it })
                WearScreen.Files -> FileBrowserScreen(state.browser, viewModel::openItem, viewModel::goUp) { screen.value = WearScreen.Home }
                WearScreen.Media -> MediaScreen { screen.value = WearScreen.Home }
                WearScreen.Remote -> RemoteScreen(state.remoteSession.state, state.remoteSession.url, state.remoteSession.pin, viewModel::startRemoteServer, viewModel::stopRemoteServer) { screen.value = WearScreen.Home }
                WearScreen.Settings -> SettingsScreen(state.batterySaverEnabled, viewModel::setBatterySaver) { screen.value = WearScreen.Home }
                WearScreen.Advanced -> AdvancedScreen(state.advancedModeEnabled, viewModel::setAdvancedMode) { screen.value = WearScreen.Home }
            }
        }
    }
}

@Composable
private fun HomeScreen(onOpen: (WearScreen) -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { Text("FileManager", textAlign = TextAlign.Center) }
        item { HomeChip("Files", onClick = { onOpen(WearScreen.Files) }) }
        item { HomeChip("Media", onClick = { onOpen(WearScreen.Media) }) }
        item { HomeChip("Remote", onClick = { onOpen(WearScreen.Remote) }) }
        item { HomeChip("Settings", onClick = { onOpen(WearScreen.Settings) }) }
        item { HomeChip("Advanced", onClick = { onOpen(WearScreen.Advanced) }) }
    }
}

@Composable
private fun HomeChip(label: String, onClick: () -> Unit) {
    Chip(label = { Text(label) }, onClick = onClick, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun FileBrowserScreen(state: BrowserState, onOpen: (String, FileItemType) -> Unit, onUp: () -> Unit, onHome: () -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { Text("Files", textAlign = TextAlign.Center) }
        item { Text(state.currentPath, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center) }
        item { Chip(label = { Text("Up") }, onClick = onUp, modifier = Modifier.fillMaxWidth()) }
        if (state.isLoading) item { Text("Loading…") }
        state.error?.let { item { Text(it, color = Color.Red, textAlign = TextAlign.Center) } }
        items(state.items.size) { index -> FileRow(state.items[index], onOpen) }
        item { Button(onClick = onHome) { Text("Home") } }
    }
}

@Composable
private fun FileRow(item: FileItem, onOpen: (String, FileItemType) -> Unit) {
    val icon = when (item.type) {
        FileItemType.Directory -> "📁"
        FileItemType.Image -> "🖼"
        FileItemType.Video -> "🎬"
        FileItemType.Audio -> "🎵"
        FileItemType.Archive -> "🗜"
        FileItemType.Document -> "📄"
        FileItemType.Other -> "•"
    }
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onOpen(item.path, item.type) }.padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(icon)
        Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun MediaScreen(onBack: () -> Unit) = SimpleListScreen("Media", listOf("Gallery", "Audio", "Video", "Recent", "Large files"), onBack)

@Composable
private fun RemoteScreen(state: RemoteServerState, url: String?, pin: String?, onStart: () -> Unit, onStop: () -> Unit, onBack: () -> Unit) {
    val running = state == RemoteServerState.Running
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { Text("Remote") }
        item { Text(if (running) "Server running" else "Server stopped", textAlign = TextAlign.Center) }
        if (url != null) item { Text(url, textAlign = TextAlign.Center) }
        if (pin != null) item { Text("PIN $pin", textAlign = TextAlign.Center) }
        item { Chip(label = { Text(if (running) "Stop server" else "Start HTTP") }, onClick = if (running) onStop else onStart, modifier = Modifier.fillMaxWidth()) }
        item { Text("Local trusted networks only", textAlign = TextAlign.Center) }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}

@Composable
private fun SettingsScreen(batterySaver: Boolean, onBatterySaver: (Boolean) -> Unit, onBack: () -> Unit) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        item { Text("Settings") }
        item { ToggleRow("Battery safe", batterySaver, onBatterySaver) }
        item { Text("Permissions") }
        item { Text("Theme: dark") }
        item { Text("Haptics: on") }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}

@Composable
private fun AdvancedScreen(enabled: Boolean, onEnabled: (Boolean) -> Unit, onBack: () -> Unit) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        item { Text("Advanced") }
        item { ToggleRow("ADB mode", enabled, onEnabled) }
        item { Text("Use ADB/root carefully. Normal apps cannot access protected system folders.", textAlign = TextAlign.Center) }
        item { Text("Diagnostics") }
        item { Text("Export logs") }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onChecked)
    }
}

@Composable
private fun SimpleListScreen(title: String, items: List<String>, onBack: () -> Unit) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        item { Text(title, textAlign = TextAlign.Center) }
        items(items.size) { index -> Text(items[index], modifier = Modifier.padding(6.dp), textAlign = TextAlign.Center) }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}
