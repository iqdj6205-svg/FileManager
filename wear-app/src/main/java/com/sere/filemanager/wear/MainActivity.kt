package com.sere.filemanager.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.sere.filemanager.core.files.StorageFormatter
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearEmpty
import com.sere.filemanager.wear.ui.wearError
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearLoading
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearSecondaryAction
import com.sere.filemanager.wear.ui.wearTitle

class MainActivity : ComponentActivity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { WearFileManagerApp() } } }

private enum class WearScreen { Home, Files, FileActions, Rename, FileDetails, ConfirmDelete, Media, Gallery, Audio, Video, ImagePreview, MediaPlayer, Remote, RemoteSettings, Settings, Advanced, Permissions, AdbGuide, StorageRoots }

private class WearNavigator(start: WearScreen = WearScreen.Home) {
    var current by mutableStateOf(start)
        private set
    private val backStack = mutableStateListOf<WearScreen>()

    fun go(screen: WearScreen) {
        if (screen == current) return
        backStack.add(current)
        current = screen
    }

    fun replace(screen: WearScreen) { current = screen }

    fun back() {
        current = backStack.removeLastOrNull() ?: WearScreen.Home
    }

    fun home() {
        backStack.clear()
        current = WearScreen.Home
    }
}

@Composable fun WearFileManagerApp() { val context = LocalContext.current; val viewModel: FileManagerViewModel = viewModel(factory = FileManagerViewModelFactory(context)); WearFileManagerContent(viewModel) }

@Composable private fun WearFileManagerContent(viewModel: FileManagerViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, viewModel) { val observer = LifecycleEventObserver { _, event -> if (event == Lifecycle.Event.ON_RESUME) viewModel.refreshPermissionState(showMessage = false) }; lifecycleOwner.lifecycle.addObserver(observer); onDispose { lifecycleOwner.lifecycle.removeObserver(observer) } }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results -> viewModel.onPermissionsResult(results) }
    val permissionActions = remember(permissionLauncher) { RuntimePermissionActions(permissionLauncher) }
    val nav = remember { WearNavigator() }
    val remoteSettingsController = remember { WearRemoteSettingsController() }
    val remoteSettings = remember { mutableStateOf(remoteSettingsController.currentUiState()) }
    val state by viewModel.state.collectAsState()
    val selected = viewModel.selectedItem()
    val permissionHubState = remember(state.permissions) { WearPermissionHubState(mediaGranted = state.permissions.mediaGranted, storageGranted = state.permissions.storageGranted, notificationsGranted = state.permissions.notificationsGranted) }

    BackHandler(enabled = nav.current != WearScreen.Home) { nav.back() }

    MaterialTheme { Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        state.operation.message?.let { OperationMessageScreen(message = it, inProgress = state.operation.inProgress, onDismiss = viewModel::clearMessage); return@Box }
        if (state.operation.inProgress) { OperationMessageScreen(message = "Working…", inProgress = true, onDismiss = {}) ; return@Box }
        when (nav.current) {
            WearScreen.Home -> HomeScreen(onOpen = nav::go)
            WearScreen.Files -> FileBrowserScreen(state.browser, state.wearOperations.clipboard, permissionHubState.allBasicGranted, { path, type -> if (type == FileItemType.Directory) viewModel.openItem(path, type) else { viewModel.selectItem(path); nav.go(WearScreen.FileActions) } }, { item -> viewModel.selectItem(item.path); nav.go(WearScreen.FileActions) }, viewModel::goUp, viewModel::pasteClipboardHere, viewModel::clearClipboard, { viewModel.createQuickFolder() }, { nav.go(WearScreen.Permissions) }, nav::home)
            WearScreen.StorageRoots -> WearStorageRootsScreen(state.storageRoots, { root -> viewModel.selectStorageRoot(root); nav.go(WearScreen.Files) }, nav::back)
            WearScreen.FileActions -> selected?.let { item -> FileActionSheet(item, { nav.go(WearScreen.FileDetails) }, { nav.go(WearScreen.Rename) }, { viewModel.markSelectedForCopy(); nav.go(WearScreen.Files) }, { viewModel.markSelectedForMove(); nav.go(WearScreen.Files) }, { nav.go(WearScreen.ConfirmDelete) }, { viewModel.toggleFavoriteSelected(); nav.go(WearScreen.Files) }, nav::back) } ?: nav.replace(WearScreen.Files)
            WearScreen.Rename -> selected?.let { item -> RenamePresetScreen(item, { newName -> viewModel.renameSelected(newName); nav.go(WearScreen.Files) }, nav::back) } ?: nav.replace(WearScreen.Files)
            WearScreen.FileDetails -> selected?.let { FileDetailsScreen(it, StorageFormatter.bytes(it.sizeBytes), nav::back) } ?: nav.replace(WearScreen.Files)
            WearScreen.ConfirmDelete -> selected?.let { ConfirmDeleteScreen(it.name, { viewModel.deleteSelected(); nav.go(WearScreen.Files) }, nav::back) } ?: nav.replace(WearScreen.Files)
            WearScreen.Media -> { LaunchedEffect(Unit) { if (state.media.images.isEmpty() && state.media.audio.isEmpty() && state.media.video.isEmpty()) viewModel.loadWearMedia() }; MediaHomeScreen(permissionHubState.mediaGranted, state.media.message, viewModel::loadWearMedia, { nav.go(WearScreen.Gallery) }, { nav.go(WearScreen.Audio) }, { nav.go(WearScreen.Video) }, { nav.go(WearScreen.Permissions) }, nav::back) }
            WearScreen.Gallery -> WearMediaLibraryScreen("Gallery", state.media.images, state.media.isLoading, state.media.message, viewModel::loadWearMedia, { item -> viewModel.openImagePreview(item); nav.go(WearScreen.ImagePreview) }, nav::back)
            WearScreen.Audio -> WearMediaLibraryScreen("Audio", state.media.audio, state.media.isLoading, state.media.message, viewModel::loadWearMedia, { item -> viewModel.selectWearMedia(item); nav.go(WearScreen.MediaPlayer) }, nav::back)
            WearScreen.Video -> WearMediaLibraryScreen("Video", state.media.video, state.media.isLoading, state.media.message, viewModel::loadWearMedia, { item -> viewModel.selectWearMedia(item); nav.go(WearScreen.MediaPlayer) }, nav::back)
            WearScreen.ImagePreview -> WearImagePreviewScreen(state.imagePreview, viewModel::imageZoomToggle, viewModel::imageRotateLeft, viewModel::imageRotateRight, nav::back)
            WearScreen.MediaPlayer -> WearPlaybackControls(state.playback, viewModel::playbackPlayPause, viewModel::playbackSeekBack, viewModel::playbackSeekForward, viewModel::playbackStop, notification = state.mediaSession.notification, onBack = nav::back)
            WearScreen.Remote -> WearRemoteDashboard(session = state.remoteSession, networkLabel = NetworkStatus(context).connectionLabel(), batteryPercent = BatteryMonitor(context).batteryPercent(), onStart = viewModel::startRemoteServer, onStop = viewModel::stopRemoteServer, onSettings = { nav.go(WearScreen.RemoteSettings) }, onBack = nav::back)
            WearScreen.RemoteSettings -> WearRemoteSettingsScreen(remoteSettings.value, { remoteSettings.value = remoteSettingsController.toggleUploads() }, { remoteSettings.value = remoteSettingsController.toggleDelete() }, { remoteSettings.value = remoteSettingsController.togglePin() }, nav::back)
            WearScreen.Settings -> SettingsScreen(state.batterySaverEnabled, viewModel::setBatterySaver, { nav.go(WearScreen.Permissions) }, { nav.go(WearScreen.StorageRoots) }, { nav.go(WearScreen.RemoteSettings) }, nav::back)
            WearScreen.Advanced -> AdvancedScreen(state.advancedModeEnabled, viewModel::setAdvancedMode, { nav.go(WearScreen.AdbGuide) }, nav::back)
            WearScreen.Permissions -> WearPermissionHub(permissionHubState, { permissionActions.requestMedia() }, { permissionActions.requestStorage() }, { permissionActions.requestNotifications() }, { nav.go(WearScreen.AdbGuide) }, nav::back)
            WearScreen.AdbGuide -> WearAdbGuideScreen(nav::back)
        }
    } }
}

@Composable private fun HomeScreen(onOpen: (WearScreen) -> Unit) { WearRotaryList { wearTitle("FileManager", "Standalone Wear file manager"); wearPrimaryAction("Files") { onOpen(WearScreen.Files) }; wearPrimaryAction("Storage") { onOpen(WearScreen.StorageRoots) }; wearPrimaryAction("Media") { onOpen(WearScreen.Media) }; wearPrimaryAction("Remote") { onOpen(WearScreen.Remote) }; wearSecondaryAction("Settings") { onOpen(WearScreen.Settings) }; wearSecondaryAction("Advanced") { onOpen(WearScreen.Advanced) } } }
@Composable private fun FileBrowserScreen(state: BrowserState, clipboard: WearClipboardState, hasPermissions: Boolean, onOpen: (String, FileItemType) -> Unit, onLongAction: (FileItem) -> Unit, onUp: () -> Unit, onPaste: () -> Unit, onClearClipboard: () -> Unit, onCreateFolder: () -> Unit, onRequestPermissions: () -> Unit, onHome: () -> Unit) { WearRotaryList { wearTitle("Files", state.currentPath); if (clipboard.hasEntry) { wearInfo("${clipboard.mode}: ${clipboard.fileName}"); wearPrimaryAction("Paste here", onPaste); wearSecondaryAction("Clear clipboard", onClearClipboard) }; if (!hasPermissions) wearPrimaryAction("Grant access", onRequestPermissions); wearSecondaryAction("Up", onUp); wearSecondaryAction("New folder", onCreateFolder); wearLoading(state.isLoading); wearError(state.error); wearEmpty(!state.isLoading && state.items.isEmpty(), if (hasPermissions) "No visible files here." else "No visible files. Grant access or choose another folder."); items(state.items.size) { index -> FileRow(state.items[index], onOpen, onLongAction) }; wearBackAction(onHome, "Home") } }
@Composable private fun FileRow(item: FileItem, onOpen: (String, FileItemType) -> Unit, onLongAction: (FileItem) -> Unit) { val icon = when (item.type) { FileItemType.Directory -> "[DIR]"; FileItemType.Image -> "[IMG]"; FileItemType.Video -> "[VID]"; FileItemType.Audio -> "[AUD]"; FileItemType.Archive -> "[ZIP]"; FileItemType.Document -> "[DOC]"; FileItemType.Other -> "[FILE]" }; Row(modifier = Modifier.fillMaxWidth().clickable { onOpen(item.path, item.type) }.padding(vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) { Text(icon, modifier = Modifier.clickable { onLongAction(item) }); Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis) } }
@Composable private fun RenamePresetScreen(item: FileItem, onRename: (String) -> Unit, onBack: () -> Unit) { WearRotaryList { wearTitle("Rename", item.name); wearInfo("Temporary presets until keyboard/voice input is wired."); wearPrimaryAction(QuickTextInputPresets.copyName(item.name)) { onRename(QuickTextInputPresets.copyName(item.name)) }; wearSecondaryAction("lowercase") { onRename(item.name.lowercase()) }; wearSecondaryAction("UPPERCASE") { onRename(item.name.uppercase()) }; wearBackAction(onBack) } }
@Composable private fun OperationMessageScreen(message: String, inProgress: Boolean, onDismiss: () -> Unit) { WearRotaryList { wearTitle(if (inProgress) "Please wait" else "Message"); wearInfo(message); if (inProgress) item { CircularProgressIndicator() } else wearBackAction(onDismiss, "OK") } }
@Composable private fun MediaHomeScreen(mediaGranted: Boolean, message: String?, onRefresh: () -> Unit, onGallery: () -> Unit, onAudio: () -> Unit, onVideo: () -> Unit, onRequestPermissions: () -> Unit, onBack: () -> Unit) { WearRotaryList { wearTitle("Media"); wearInfo(message); wearPrimaryAction("Refresh", onRefresh); if (!mediaGranted) wearSecondaryAction("Grant media", onRequestPermissions); wearPrimaryAction("Gallery", onGallery); wearPrimaryAction("Audio", onAudio); wearPrimaryAction("Video", onVideo); wearBackAction(onBack) } }
@Composable private fun SettingsScreen(batterySaver: Boolean, onBatterySaver: (Boolean) -> Unit, onPermissions: () -> Unit, onStorage: () -> Unit, onRemoteSettings: () -> Unit, onBack: () -> Unit) { WearRotaryList { wearTitle("Settings"); item { ToggleRow("Battery safe", batterySaver, onBatterySaver) }; wearPrimaryAction("Permissions", onPermissions); wearPrimaryAction("Storage roots", onStorage); wearSecondaryAction("Remote settings", onRemoteSettings); wearInfo("Theme: dark"); wearInfo("Haptics: on"); wearBackAction(onBack) } }
@Composable private fun AdvancedScreen(enabled: Boolean, onEnabled: (Boolean) -> Unit, onAdbGuide: () -> Unit, onBack: () -> Unit) { WearRotaryList { wearTitle("Advanced"); item { ToggleRow("ADB mode", enabled, onEnabled) }; wearPrimaryAction("ADB guide", onAdbGuide); wearInfo("Use ADB/root carefully. Normal apps cannot access protected system folders."); wearInfo("Diagnostics pending"); wearBackAction(onBack) } }
@Composable private fun ToggleRow(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) { Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(label, modifier = Modifier.weight(1f)); Switch(checked = checked, onCheckedChange = onChecked) } }
