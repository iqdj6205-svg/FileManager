package com.sere.filemanager.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.files.StorageFormatter
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { WearFileManagerApp() } } }

private enum class WearScreen { Home, Files, FileActions, FileDetails, ConfirmDelete, Media, Gallery, Audio, Video, Remote, RemoteSettings, Settings, Advanced, Permissions, AdbGuide, StorageAccess }

@Composable fun WearFileManagerApp() { val context = LocalContext.current; val viewModel: FileManagerViewModel = viewModel(factory = FileManagerViewModelFactory(context)); WearFileManagerContent(viewModel) }

@Composable
private fun WearFileManagerContent(viewModel: FileManagerViewModel) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results -> viewModel.onPermissionsResult(results) }
    val permissionActions = remember(permissionLauncher) { RuntimePermissionActions(permissionLauncher) }
    val screen = remember { mutableStateOf(WearScreen.Home) }
    val remoteSettingsController = remember { WearRemoteSettingsController() }
    val remoteSettings = remember { mutableStateOf(remoteSettingsController.currentUiState()) }
    val state by viewModel.state.collectAsState()
    val selected = viewModel.selectedItem()
    val permissionHubState = remember(state.permissions) { WearPermissionHubState(mediaGranted = state.permissions.mediaGranted, storageGranted = state.permissions.storageGranted, notificationsGranted = state.permissions.notificationsGranted) }

    BackHandler(enabled = screen.value != WearScreen.Home) {
        screen.value = when (screen.value) {
            WearScreen.FileActions, WearScreen.FileDetails, WearScreen.ConfirmDelete -> WearScreen.Files
            WearScreen.Gallery, WearScreen.Audio, WearScreen.Video -> WearScreen.Media
            WearScreen.Permissions, WearScreen.StorageAccess, WearScreen.AdbGuide, WearScreen.RemoteSettings -> WearScreen.Settings
            else -> WearScreen.Home
        }
    }

    MaterialTheme { Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        state.operation.message?.let { OperationMessageScreen(message = it, onDismiss = viewModel::clearMessage); return@Box }
        when (screen.value) {
            WearScreen.Home -> HomeScreen(onOpen = { screen.value = it })
            WearScreen.Files -> FileBrowserScreen(state.browser, permissionHubState.allBasicGranted, { path, type -> if (type == FileItemType.Directory) viewModel.openItem(path, type) else { viewModel.selectItem(path); screen.value = WearScreen.FileActions } }, { item -> viewModel.selectItem(item.path); screen.value = WearScreen.FileActions }, viewModel::goUp, { viewModel.createQuickFolder() }, { screen.value = WearScreen.Permissions }, { screen.value = WearScreen.Home })
            WearScreen.FileActions -> selected?.let { item -> FileActionSheet(item, { screen.value = WearScreen.FileDetails }, { viewModel.renameSelected(QuickTextInputPresets.copyName(item.name)); screen.value = WearScreen.Files }, { viewModel.copySelected(); screen.value = WearScreen.Files }, { viewModel.copySelected(); screen.value = WearScreen.Files }, { screen.value = WearScreen.ConfirmDelete }, { viewModel.toggleFavoriteSelected(); screen.value = WearScreen.Files }, { screen.value = WearScreen.Files }) } ?: run { screen.value = WearScreen.Files }
            WearScreen.FileDetails -> selected?.let { FileDetailsScreen(it, StorageFormatter.bytes(it.sizeBytes)) { screen.value = WearScreen.FileActions } } ?: run { screen.value = WearScreen.Files }
            WearScreen.ConfirmDelete -> selected?.let { ConfirmDeleteScreen(it.name, { viewModel.deleteSelected(); screen.value = WearScreen.Files }, { screen.value = WearScreen.FileActions }) } ?: run { screen.value = WearScreen.Files }
            WearScreen.Media -> MediaHomeScreen(permissionHubState.mediaGranted, { screen.value = WearScreen.Gallery }, { screen.value = WearScreen.Audio }, { screen.value = WearScreen.Video }, { screen.value = WearScreen.Permissions }, { screen.value = WearScreen.Home })
            WearScreen.Gallery -> MediaListScreen("Gallery", state.browser.items.filter { it.type == FileItemType.Image }) { screen.value = WearScreen.Media }
            WearScreen.Audio -> MediaListScreen("Audio", state.browser.items.filter { it.type == FileItemType.Audio }) { screen.value = WearScreen.Media }
            WearScreen.Video -> MediaListScreen("Video", state.browser.items.filter { it.type == FileItemType.Video }) { screen.value = WearScreen.Media }
            WearScreen.Remote -> WearRemoteDashboard(session = state.remoteSession, networkLabel = NetworkStatus(context).connectionLabel(), batteryPercent = BatteryMonitor(context).batteryPercent(), onStart = viewModel::startRemoteServer, onStop = viewModel::stopRemoteServer, onSettings = { screen.value = WearScreen.RemoteSettings }, onBack = { screen.value = WearScreen.Home })
            WearScreen.RemoteSettings -> WearRemoteSettingsScreen(remoteSettings.value, { remoteSettings.value = remoteSettingsController.toggleUploads() }, { remoteSettings.value = remoteSettingsController.toggleDelete() }, { remoteSettings.value = remoteSettingsController.togglePin() }, { screen.value = WearScreen.Settings })
            WearScreen.Settings -> SettingsScreen(state.batterySaverEnabled, viewModel::setBatterySaver, { screen.value = WearScreen.Permissions }, { screen.value = WearScreen.StorageAccess }, { screen.value = WearScreen.RemoteSettings }, { screen.value = WearScreen.Home })
            WearScreen.Advanced -> AdvancedScreen(state.advancedModeEnabled, viewModel::setAdvancedMode, { screen.value = WearScreen.AdbGuide }) { screen.value = WearScreen.Home }
            WearScreen.Permissions -> WearPermissionHub(permissionHubState, { permissionActions.requestMedia() }, { permissionActions.requestMedia() }, { permissionActions.requestNotifications() }, { screen.value = WearScreen.AdbGuide }, { screen.value = WearScreen.Settings })
            WearScreen.AdbGuide -> WearAdbGuideScreen { screen.value = WearScreen.Advanced }
            WearScreen.StorageAccess -> StorageAccessScreen(onMedia = { screen.value = WearScreen.Permissions }, onAdvanced = { screen.value = WearScreen.Advanced }, onBack = { screen.value = WearScreen.Settings })
        }
    } }
}

@Composable private fun RotaryScalingLazyColumn(modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(18.dp), horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally, content: ScalingLazyListScope.() -> Unit) { val state = rememberScalingLazyListState(); val focusRequester = remember { FocusRequester() }; val scope = rememberCoroutineScope(); LaunchedEffect(Unit) { focusRequester.requestFocus() }; ScalingLazyColumn(state = state, modifier = modifier.fillMaxSize().focusRequester(focusRequester).focusable().onRotaryScrollEvent { val nextIndex = (state.centerItemIndex + if (it.verticalScrollPixels > 0) 1 else -1).coerceAtLeast(0); scope.launch { state.scrollToItem(nextIndex) }; true }, contentPadding = contentPadding, horizontalAlignment = horizontalAlignment, flingBehavior = ScrollableDefaults.flingBehavior(), content = content) }
@Composable private fun HomeScreen(onOpen: (WearScreen) -> Unit) { RotaryScalingLazyColumn(contentPadding = PaddingValues(horizontal = 18.dp, vertical = 28.dp)) { item { Text("FileManager", textAlign = TextAlign.Center) }; item { HomeChip("Files") { onOpen(WearScreen.Files) } }; item { HomeChip("Media") { onOpen(WearScreen.Media) } }; item { HomeChip("Remote") { onOpen(WearScreen.Remote) } }; item { HomeChip("Settings") { onOpen(WearScreen.Settings) } }; item { HomeChip("Advanced") { onOpen(WearScreen.Advanced) } } } }
@Composable private fun HomeChip(label: String, onClick: () -> Unit) { Chip(label = { Text(label) }, onClick = onClick, modifier = Modifier.fillMaxWidth()) }
@Composable private fun FileBrowserScreen(state: BrowserState, hasPermissions: Boolean, onOpen: (String, FileItemType) -> Unit, onLongAction: (FileItem) -> Unit, onUp: () -> Unit, onCreateFolder: () -> Unit, onRequestPermissions: () -> Unit, onHome: () -> Unit) { RotaryScalingLazyColumn(contentPadding = PaddingValues(horizontal = 14.dp, vertical = 22.dp)) { item { Text("Files", textAlign = TextAlign.Center) }; item { Text(state.currentPath, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center) }; if (!hasPermissions) item { Chip(label = { Text("Grant access") }, onClick = onRequestPermissions, modifier = Modifier.fillMaxWidth()) }; item { Chip(label = { Text("Up") }, onClick = onUp, modifier = Modifier.fillMaxWidth()) }; item { Chip(label = { Text("New folder") }, onClick = onCreateFolder, modifier = Modifier.fillMaxWidth()) }; if (state.isLoading) item { Text("Loading…") }; state.error?.let { item { Text(it, color = Color.Red, textAlign = TextAlign.Center) } }; if (!state.isLoading && state.items.isEmpty()) item { Text(if (hasPermissions) "No visible files here." else "No visible files. Grant access or choose another folder.", textAlign = TextAlign.Center) }; items(state.items.size) { index -> FileRow(state.items[index], onOpen, onLongAction) }; item { Button(onClick = onHome) { Text("Home") } } } }
@Composable private fun FileRow(item: FileItem, onOpen: (String, FileItemType) -> Unit, onLongAction: (FileItem) -> Unit) { val icon = when (item.type) { FileItemType.Directory -> "📁"; FileItemType.Image -> "🖼"; FileItemType.Video -> "🎬"; FileItemType.Audio -> "🎵"; FileItemType.Archive -> "🗜"; FileItemType.Document -> "📄"; FileItemType.Other -> "•" }; Row(modifier = Modifier.fillMaxWidth().clickable { onOpen(item.path, item.type) }.padding(vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) { Text(icon, modifier = Modifier.clickable { onLongAction(item) }); Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis) } }
@Composable private fun OperationMessageScreen(message: String, onDismiss: () -> Unit) { RotaryScalingLazyColumn { item { Text(message, textAlign = TextAlign.Center) }; item { Button(onClick = onDismiss) { Text("OK") } } } }
@Composable private fun MediaHomeScreen(mediaGranted: Boolean, onGallery: () -> Unit, onAudio: () -> Unit, onVideo: () -> Unit, onRequestPermissions: () -> Unit, onBack: () -> Unit) { RotaryScalingLazyColumn { item { Text("Media") }; if (!mediaGranted) item { Chip(label = { Text("Grant media") }, onClick = onRequestPermissions, modifier = Modifier.fillMaxWidth()) }; item { Chip(label = { Text("Gallery") }, onClick = onGallery, modifier = Modifier.fillMaxWidth()) }; item { Chip(label = { Text("Audio") }, onClick = onAudio, modifier = Modifier.fillMaxWidth()) }; item { Chip(label = { Text("Video") }, onClick = onVideo, modifier = Modifier.fillMaxWidth()) }; item { Button(onClick = onBack) { Text("Back") } } } }
@Composable private fun MediaListScreen(title: String, items: List<FileItem>, onBack: () -> Unit) { RotaryScalingLazyColumn { item { Text(title) }; if (items.isEmpty()) item { Text("No items in current folder", textAlign = TextAlign.Center) }; items(items.size) { index -> Text(items[index].name, maxLines = 1, overflow = TextOverflow.Ellipsis) }; item { Button(onClick = onBack) { Text("Back") } } } }
@Composable private fun SettingsScreen(batterySaver: Boolean, onBatterySaver: (Boolean) -> Unit, onPermissions: () -> Unit, onStorage: () -> Unit, onRemoteSettings: () -> Unit, onBack: () -> Unit) { RotaryScalingLazyColumn { item { Text("Settings") }; item { ToggleRow("Battery safe", batterySaver, onBatterySaver) }; item { Chip(label = { Text("Permissions") }, onClick = onPermissions, modifier = Modifier.fillMaxWidth()) }; item { Chip(label = { Text("Storage access") }, onClick = onStorage, modifier = Modifier.fillMaxWidth()) }; item { Chip(label = { Text("Remote settings") }, onClick = onRemoteSettings, modifier = Modifier.fillMaxWidth()) }; item { Text("Theme: dark") }; item { Text("Haptics: on") }; item { Button(onClick = onBack) { Text("Back") } } } }
@Composable private fun AdvancedScreen(enabled: Boolean, onEnabled: (Boolean) -> Unit, onAdbGuide: () -> Unit, onBack: () -> Unit) { RotaryScalingLazyColumn { item { Text("Advanced") }; item { ToggleRow("ADB mode", enabled, onEnabled) }; item { Chip(label = { Text("ADB guide") }, onClick = onAdbGuide, modifier = Modifier.fillMaxWidth()) }; item { Text("Use ADB/root carefully. Normal apps cannot access protected system folders.", textAlign = TextAlign.Center) }; item { Text("Diagnostics") }; item { Button(onClick = onBack) { Text("Back") } } } }
@Composable private fun ToggleRow(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) { Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(label); Switch(checked = checked, onCheckedChange = onChecked) } }
