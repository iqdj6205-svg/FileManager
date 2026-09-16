package com.sere.filemanager.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WearFileManagerApp() }
    }
}

private enum class WearScreen { Home, Files, Media, Remote, Settings, Advanced }

@Composable
fun WearFileManagerApp() {
    val screen = remember { mutableStateOf(WearScreen.Home) }
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            when (screen.value) {
                WearScreen.Home -> HomeScreen(onOpen = { screen.value = it })
                WearScreen.Files -> SimpleListScreen("Files", listOf("Internal storage", "Media", "Downloads", "Favorites")) { screen.value = WearScreen.Home }
                WearScreen.Media -> SimpleListScreen("Media", listOf("Gallery", "Audio", "Video", "Recent")) { screen.value = WearScreen.Home }
                WearScreen.Remote -> SimpleListScreen("Remote", listOf("HTTP server: off", "Start server", "Pair by QR", "Security PIN")) { screen.value = WearScreen.Home }
                WearScreen.Settings -> SimpleListScreen("Settings", listOf("Permissions", "Battery policy", "Theme", "Haptics")) { screen.value = WearScreen.Home }
                WearScreen.Advanced -> SimpleListScreen("Advanced", listOf("ADB guide", "Storage limits", "Diagnostics", "Export logs")) { screen.value = WearScreen.Home }
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
        item { Chip(label = { Text("Files") }, onClick = { onOpen(WearScreen.Files) }, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Media") }, onClick = { onOpen(WearScreen.Media) }, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Remote") }, onClick = { onOpen(WearScreen.Remote) }, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Settings") }, onClick = { onOpen(WearScreen.Settings) }, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Advanced") }, onClick = { onOpen(WearScreen.Advanced) }, modifier = Modifier.fillMaxWidth()) }
    }
}

@Composable
private fun SimpleListScreen(title: String, items: List<String>, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, textAlign = TextAlign.Center)
        ScalingLazyColumn(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            items(items.size) { index -> Text(items[index], modifier = Modifier.padding(6.dp), textAlign = TextAlign.Center) }
        }
        Button(onClick = onBack) { Text("Back") }
    }
}
