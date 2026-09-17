package com.sere.filemanager.phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sere.filemanager.core.model.DefaultHomeActions
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import com.sere.filemanager.core.ui.FileManagerTheme
import com.sere.filemanager.core.ui.UiFormatters

class MainActivity : ComponentActivity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { PhoneFileManagerApp() } } }

@Composable
fun PhoneFileManagerApp() {
    val context = LocalContext.current
    val viewModel: PhoneViewModel = viewModel(factory = PhoneViewModelFactory(context))
    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> if (uri != null) viewModel.onPickedFile(uri) }
    val screen = remember { mutableStateOf(PhoneScreen.Home) }
    val state by viewModel.state.collectAsState()
    val selected = state.fileActions.selected
    val selectedMedia = state.media.selected
    BackHandler(enabled = screen.value != PhoneScreen.Home) { screen.value = PhoneScreen.Home }
    FileManagerTheme {
        when (screen.value) {
            PhoneScreen.Home -> PhoneHomeScreen(state.statusMessage, { screen.value = PhoneScreen.PhoneFiles }, { screen.value = PhoneScreen.PhoneMedia }, { screen.value = PhoneScreen.StorageAnalyzer }, { screen.value = PhoneScreen.WatchCompanion }, { screen.value = PhoneScreen.RemoteManager }, { screen.value = PhoneScreen.RemoteSettings }, { screen.value = PhoneScreen.Settings })
            PhoneScreen.PhoneFiles -> PhoneFilesScreen(state.browser, { path, type -> if (type == FileItemType.Directory) viewModel.openPhoneItem(path, type) else { viewModel.selectPhoneFile(path); screen.value = PhoneScreen.PhoneFileActions } }, viewModel::phoneGoUp) { screen.value = PhoneScreen.Home }
            PhoneScreen.PhoneFileActions -> selected?.let { PhoneFileActionScreen(it, { screen.value = PhoneScreen.PhoneFileDetails }, { viewModel.renameSelectedPhoneFileAsCopy(); screen.value = PhoneScreen.PhoneFiles }, { viewModel.copySelectedPhoneFile(); screen.value = PhoneScreen.PhoneFiles }, { screen.value = PhoneScreen.PhoneConfirmDelete }, { screen.value = PhoneScreen.PhoneFiles }) } ?: run { screen.value = PhoneScreen.PhoneFiles }
            PhoneScreen.PhoneFileDetails -> selected?.let { PhoneFileDetailsScreen(it) { screen.value = PhoneScreen.PhoneFileActions } } ?: run { screen.value = PhoneScreen.PhoneFiles }
            PhoneScreen.PhoneConfirmDelete -> selected?.let { PhoneConfirmDeleteScreen(it.name, { viewModel.deleteSelectedPhoneFile(); screen.value = PhoneScreen.PhoneFiles }, { screen.value = PhoneScreen.PhoneFileActions }) } ?: run { screen.value = PhoneScreen.PhoneFiles }
            PhoneScreen.PhoneMedia -> { LaunchedEffect(Unit) { if (state.media.items.isEmpty()) viewModel.loadPhoneMedia() }; PhoneMediaLibraryScreen(state.media, viewModel::loadPhoneMedia, { item -> viewModel.selectMedia(item); screen.value = PhoneScreen.PhoneMediaPreview }) { screen.value = PhoneScreen.Home } }
            PhoneScreen.PhoneMediaPreview -> selectedMedia?.let { PhoneMediaPreviewScreen(it) { screen.value = PhoneScreen.PhoneMedia } } ?: run { screen.value = PhoneScreen.PhoneMedia }
            PhoneScreen.StorageAnalyzer -> PhoneStorageAnalyzerScreen(state.analyzer, viewModel::analyzeCurrentPhoneFolder) { screen.value = PhoneScreen.Home }
            PhoneScreen.WatchCompanion -> WatchCompanionScreen(state.remoteUrl, state.statusMessage, viewModel::setRemoteUrl, viewModel::startPairing, viewModel::startRemoteServer, viewModel::stopRemoteServer, viewModel::requestWatchStatus, viewModel::refreshRemoteSnapshot, { filePicker.launch(arrayOf("*/*")) }) { screen.value = PhoneScreen.Home }
            PhoneScreen.RemoteManager -> RemoteManagerScreen(state.remoteUrl, state.statusMessage, viewModel::setRemoteUrl, viewModel::openRemoteManager) { screen.value = PhoneScreen.Home }
            PhoneScreen.RemoteSettings -> RemoteSettingsScreen(state.remoteSettings, viewModel::toggleUploads, viewModel::toggleDelete, viewModel::toggleAdvancedMode, viewModel::toggleShowHidden, viewModel::syncSettingsToWatch) { screen.value = PhoneScreen.Home }
            PhoneScreen.Settings -> PlaceholderScreen("Settings", "Phone file manager settings will be configured here.") { screen.value = PhoneScreen.Home }
        }
    }
}

@Composable private fun PhoneHomeScreen(status: String, onFiles: () -> Unit, onMedia: () -> Unit, onAnalyzer: () -> Unit, onWatch: () -> Unit, onRemote: () -> Unit, onRemoteSettings: () -> Unit, onSettings: () -> Unit) { Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { Text("FileManager", style = MaterialTheme.typography.headlineMedium); Text(status); HomeButton(DefaultHomeActions.files.emoji, "Phone files", DefaultHomeActions.files.description, onFiles); HomeButton(DefaultHomeActions.media.emoji, "Phone media", DefaultHomeActions.media.description, onMedia); HomeButton(DefaultHomeActions.analyzer.emoji, "Storage analyzer", DefaultHomeActions.analyzer.description, onAnalyzer); HomeButton("⌚", "Watch companion", "Optional Wear OS helper", onWatch); HomeButton(DefaultHomeActions.remote.emoji, "Remote manager", "Open watch/phone server tools", onRemote); HomeButton("🔐", "Remote settings", "Sync watch server permissions", onRemoteSettings); HomeButton(DefaultHomeActions.settings.emoji, "Settings", DefaultHomeActions.settings.description, onSettings) } }
@Composable private fun HomeButton(icon: String, title: String, subtitle: String, onClick: () -> Unit) { Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) { Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) { Text(icon, style = MaterialTheme.typography.headlineSmall); Column { Text(title, style = MaterialTheme.typography.titleMedium); Text(subtitle, style = MaterialTheme.typography.bodyMedium) } } } }
@Composable private fun PhoneFilesScreen(browser: PhoneFileBrowserState, onOpen: (String, FileItemType) -> Unit, onUp: () -> Unit, onBack: () -> Unit) { Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("Phone files", style = MaterialTheme.typography.headlineSmall); Text(browser.currentPath, maxLines = 1, overflow = TextOverflow.Ellipsis); Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Button(onClick = onUp) { Text("Up") }; Button(onClick = onBack) { Text("Home") } }; browser.error?.let { Text(it) }; if (browser.isLoading) Text("Loading…"); LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) { items(browser.items) { item -> PhoneFileRow(item, onOpen) } } } }
@Composable private fun PhoneFileRow(item: FileItem, onOpen: (String, FileItemType) -> Unit) { val icon = when (item.type) { FileItemType.Directory -> "📁"; FileItemType.Image -> "🖼"; FileItemType.Video -> "🎬"; FileItemType.Audio -> "🎵"; FileItemType.Archive -> "🗜"; FileItemType.Document -> "📄"; FileItemType.Other -> "•" }; Card(modifier = Modifier.fillMaxWidth().clickable { onOpen(item.path, item.type) }) { Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) { Text(icon); Column(modifier = Modifier.weight(1f)) { Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(UiFormatters.compactBytes(item.sizeBytes)) } } } }
@Composable private fun WatchCompanionScreen(remoteUrl: String, status: String, onUrl: (String) -> Unit, onPair: () -> Unit, onStart: () -> Unit, onStop: () -> Unit, onStatus: () -> Unit, onSnapshot: () -> Unit, onSend: () -> Unit, onBack: () -> Unit) { Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { Text("Watch companion", style = MaterialTheme.typography.headlineSmall); Text(status); OutlinedTextField(value = remoteUrl, onValueChange = onUrl, label = { Text("Watch HTTP URL") }, modifier = Modifier.fillMaxWidth()); Button(onClick = onPair, modifier = Modifier.fillMaxWidth()) { Text("Find watch") }; Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) { Text("Start watch server") }; Button(onClick = onStop, modifier = Modifier.fillMaxWidth()) { Text("Stop watch server") }; Button(onClick = onStatus, modifier = Modifier.fillMaxWidth()) { Text("Ask status") }; Button(onClick = onSnapshot, modifier = Modifier.fillMaxWidth()) { Text("Read latest status") }; Button(onClick = onSend, modifier = Modifier.fillMaxWidth()) { Text("Choose and send file") }; Button(onClick = onBack) { Text("Home") } } }
@Composable private fun RemoteManagerScreen(remoteUrl: String, status: String, onUrl: (String) -> Unit, onOpen: () -> Unit, onBack: () -> Unit) { Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { Text("Remote manager", style = MaterialTheme.typography.headlineSmall); Text(status); OutlinedTextField(value = remoteUrl, onValueChange = onUrl, label = { Text("Server URL") }, modifier = Modifier.fillMaxWidth()); Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text("Open") }; Button(onClick = onBack) { Text("Home") } } }
@Composable private fun PlaceholderScreen(title: String, body: String, onBack: () -> Unit) { Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { Text(title, style = MaterialTheme.typography.headlineSmall); Text(body); Button(onClick = onBack) { Text("Home") } } }
