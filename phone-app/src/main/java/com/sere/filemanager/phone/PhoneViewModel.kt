package com.sere.filemanager.phone

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sere.filemanager.core.files.*
import com.sere.filemanager.core.media.*
import com.sere.filemanager.core.model.*
import com.sere.filemanager.core.wearbridge.WearBridgeCommandType
import com.sere.filemanager.core.wearbridge.WearBridgeSettingsPayload
import com.sere.filemanager.core.wearbridge.WearBridgeSettingsStringCodec
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneViewModel(
    private val protocolClient: CompanionProtocolClient = CompanionProtocolClient(),
    private val fileRepository: FileRepository = LocalFileRepository(),
    private val wearBridgeClient: WearBridgePhoneClient? = null,
    private val transferController: PhoneTransferController? = null,
    private val fileOperationsController: PhoneFileOperationsController? = null,
    private val mediaController: PhoneMediaController? = null,
    private val playbackController: MediaPlaybackController? = null,
    private val settingsUseCase: AppSettingsUseCase = AppSettingsUseCase(InMemoryAppSettingsRepository()),
    private val storageAccessManager: StorageAccessManager = StorageAccessManager(),
    private val safController: PhoneSafController? = null,
) : ViewModel() {
    private val analyzer = StorageAnalyzer(fileRepository)
    private val insights = StorageInsights()
    private val rootResolver = StorageRootResolver()
    private val clipboard = FileClipboard()
    private val imagePreviewController = ImagePreviewController()
    private val sessionMapper = MediaSessionStateMapper()
    private val _state = MutableStateFlow(PhoneAppState())
    val state: StateFlow<PhoneAppState> = _state.asStateFlow()

    init {
        openPhonePath(_state.value.browser.currentPath)
        viewModelScope.launch { storageAccessManager.roots.collect { roots -> _state.update { it.copy(storageRoots = roots) } } }
        viewModelScope.launch { settingsUseCase.settings.collect { settings -> _state.update { it.copy(appSettings = settings, remoteSettings = PhoneRemoteSettingsState.from(settings.remoteServer, settings.advancedMode, settings.showHiddenFiles)) } } }
        playbackController?.let { controller -> viewModelScope.launch { controller.session.collect { session -> _state.update { it.copy(playback = session, mediaSession = PhoneMediaSessionState(metadata = session.item?.let(sessionMapper::metadata), notification = sessionMapper.notification(session))) } } } }
        safController?.let { controller -> viewModelScope.launch { controller.progressSink.progress.collect { progress -> _state.update { it.copy(operationProgress = PhoneOperationProgressState(progress)) } } } }
        viewModelScope.launch { PhoneTransferProgressStore.progress.collect { progress -> if (progress != null) _state.update { it.copy(transfer = it.transfer.copy(latestProgress = progress, message = progress.message), statusMessage = progress.message ?: progress.state.name) } } }
        viewModelScope.launch { PhoneRemoteStatusStore.status.collect { status -> if (status != null) _state.update { it.copy(remoteUrl = status.url ?: it.remoteUrl, statusMessage = if (status.running) "Watch server: ${status.url} PIN ${status.pin} ${status.networkLabel.orEmpty()} ${status.batteryPercent ?: ""}%" else "Watch server stopped") } } }
    }

    fun refreshTransferProgress() { val progress = PhoneTransferProgressStore.latest(); if (progress == null) _state.update { it.copy(statusMessage = "No transfer progress yet") } else _state.update { it.copy(transfer = it.transfer.copy(latestProgress = progress, message = progress.message), statusMessage = progress.message ?: progress.state.name) } }
    fun openImagePreview(item: MediaItem) { _state.update { it.copy(media = it.media.copy(selected = item), imagePreview = imagePreviewController.open(item), statusMessage = item.displayName) } }
    fun imageZoomToggle() { _state.update { it.copy(imagePreview = imagePreviewController.reduce(it.imagePreview, ImagePreviewAction.ZoomToggle)) } }
    fun imageRotateLeft() { _state.update { it.copy(imagePreview = imagePreviewController.reduce(it.imagePreview, ImagePreviewAction.RotateLeft)) } }
    fun imageRotateRight() { _state.update { it.copy(imagePreview = imagePreviewController.reduce(it.imagePreview, ImagePreviewAction.RotateRight)) } }
    fun selectStorageRoot(root: StorageRoot) { storageAccessManager.selectRoot(root.id); when { rootResolver.canUsePath(root) -> openPhonePath(root.path!!); rootResolver.canUseSaf(root) -> openSafRoot(root); else -> _state.update { it.copy(statusMessage = "${root.title} opens in its own screen") } } }
    fun addStorageTree(uri: Uri?) { if (uri == null) return; val name = safController?.persistAndName(uri) ?: "Selected folder"; storageAccessManager.addSafTree(name, uri.toString()); _state.update { it.copy(statusMessage = "Folder access added: $name") } }
    private fun openSafRoot(root: StorageRoot) { val uri = rootResolver.safUri(root) ?: return; val controller = safController ?: run { _state.update { it.copy(statusMessage = "SAF unavailable") }; return }; viewModelScope.launch { _state.update { it.copy(browser = it.browser.copy(currentPath = rootResolver.preferredDisplayPath(root), isLoading = true, error = null)) }; runCatching { controller.list(uri) }.onSuccess { items -> _state.update { it.copy(browser = it.browser.copy(items = items, isLoading = false), statusMessage = "Opened ${root.title}") } }.onFailure { error -> _state.update { it.copy(browser = it.browser.copy(isLoading = false, error = error.message ?: "Cannot open folder"), statusMessage = error.message ?: "Cannot open folder") } } } }
    fun openPhonePath(path: String) { viewModelScope.launch { _state.update { it.copy(browser = it.browser.copy(currentPath = path, isLoading = true, error = null)) }; runCatching { fileRepository.list(path) }.onSuccess { items -> _state.update { it.copy(browser = it.browser.copy(items = items, isLoading = false)) } }.onFailure { error -> _state.update { it.copy(browser = it.browser.copy(isLoading = false, error = error.message ?: "Cannot open folder")) } } } }
    fun openPhoneItem(path: String, type: FileItemType) { if (type == FileItemType.Directory) openPhonePath(path) else selectPhoneFile(path) }
    fun phoneGoUp() { val current = _state.value.browser.currentPath.trimEnd('/'); openPhonePath(current.substringBeforeLast('/', missingDelimiterValue = "/").ifBlank { "/" }) }
    fun selectPhoneFile(path: String) { val item = _state.value.browser.items.firstOrNull { it.path == path }; _state.update { it.copy(fileActions = it.fileActions.copy(selected = item), statusMessage = item?.name ?: "No file selected") } }
    fun markSelectedForCopy() = markClipboard(FileClipboardMode.Copy)
    fun markSelectedForMove() = markClipboard(FileClipboardMode.Move)
    private fun markClipboard(mode: FileClipboardMode) { val item = _state.value.fileActions.selected ?: run { _state.update { it.copy(statusMessage = "No file selected") }; return }; clipboard.set(item, mode); _state.update { it.copy(clipboard = PhoneClipboardState(item.name, mode, "${mode.name.lowercase().replaceFirstChar { c -> c.uppercase() }} queued"), statusMessage = "${item.name} queued for ${mode.name.lowercase()}") } }
    fun pasteClipboardHere() { val entry = clipboard.current() ?: run { _state.update { it.copy(statusMessage = "Clipboard empty") }; return }; val controller = fileOperationsController ?: run { _state.update { it.copy(statusMessage = "File operations unavailable") }; return }; val target = _state.value.browser.currentPath; viewModelScope.launch { _state.update { it.copy(statusMessage = "Pasting…") }; val result = when (entry.mode) { FileClipboardMode.Copy -> controller.copyTo(entry.item, target); FileClipboardMode.Move -> controller.moveTo(entry.item, target) }; if (result.success) clipboard.clear(); _state.update { it.copy(clipboard = if (result.success) PhoneClipboardState(message = "Clipboard cleared") else it.clipboard.copy(message = result.message), statusMessage = result.message ?: if (result.success) "Paste complete" else "Paste failed") }; openPhonePath(target) } }
    fun clearClipboard() { clipboard.clear(); _state.update { it.copy(clipboard = PhoneClipboardState(message = "Clipboard cleared"), statusMessage = "Clipboard cleared") } }
    fun beginCreateFolder() { _state.update { it.copy(edit = it.edit.copy(createFolderValue = "New Folder", message = null)) } }
    fun updateCreateFolderName(value: String) { _state.update { it.copy(edit = it.edit.copy(createFolderValue = value)) } }
    fun createFolderFromInput() { val name = _state.value.edit.createFolderValue.trim(); if (name.isBlank()) { _state.update { it.copy(statusMessage = "Folder name required") }; return }; val controller = fileOperationsController ?: return; val parent = _state.value.browser.currentPath; viewModelScope.launch { val result = controller.createFolder(parent, name); _state.update { it.copy(statusMessage = result.message ?: if (result.success) "Folder created" else "Create failed") }; openPhonePath(parent) } }
    fun beginRenameSelected() { val item = _state.value.fileActions.selected; _state.update { it.copy(edit = it.edit.copy(renameValue = item?.name.orEmpty(), message = null)) } }
    fun updateRenameValue(value: String) { _state.update { it.copy(edit = it.edit.copy(renameValue = value)) } }
    fun renameSelectedFromInput() { val name = _state.value.edit.renameValue.trim(); if (name.isBlank()) { _state.update { it.copy(statusMessage = "New name required") }; return }; executePhoneFileAction("Renaming…") { controller, item -> controller.rename(item, name) } }
    fun copySelectedPhoneFile() = executePhoneFileAction("Copying…") { controller, item -> controller.copyHere(item) }
    fun deleteSelectedPhoneFile() = executePhoneFileAction("Deleting…") { controller, item -> controller.delete(item) }
    private fun executePhoneFileAction(pending: String, action: suspend (PhoneFileOperationsController, FileItem) -> FileOperationResult) { val controller = fileOperationsController ?: run { _state.update { it.copy(statusMessage = "File operations unavailable") }; return }; val item = _state.value.fileActions.selected ?: run { _state.update { it.copy(statusMessage = "No file selected") }; return }; viewModelScope.launch { _state.update { it.copy(statusMessage = pending) }; val result = action(controller, item); _state.update { it.copy(fileActions = it.fileActions.copy(message = result.message), statusMessage = result.message ?: if (result.success) "Done" else "Failed") }; openPhonePath(_state.value.browser.currentPath) } }
    fun analyzeCurrentPhoneFolder() { viewModelScope.launch { val path = _state.value.browser.currentPath; _state.update { it.copy(analyzer = it.analyzer.copy(isLoading = true, message = "Analyzing $path")) }; runCatching { analyzer.analyze(path) }.onSuccess { analysis -> _state.update { it.copy(analyzer = it.analyzer.copy(analysis = analysis, insights = insights.fromAnalysis(analysis), isLoading = false, message = "Analysis complete"), statusMessage = "Analysis complete") } }.onFailure { error -> _state.update { it.copy(analyzer = it.analyzer.copy(isLoading = false, message = error.message ?: "Analysis failed"), statusMessage = error.message ?: "Analysis failed") } } } }
    fun onPermissionsResult(results: Map<String, Boolean>) { val granted = results.values.count { it }; val total = results.size; _state.update { it.copy(statusMessage = if (total == 0) "No permission needed" else "Permissions: $granted/$total granted") }; if (granted > 0 && _state.value.media.items.isEmpty()) loadPhoneMedia() }
    fun loadPhoneMedia() { val controller = mediaController ?: run { _state.update { it.copy(statusMessage = "Media unavailable") }; return }; viewModelScope.launch { _state.update { it.copy(media = it.media.copy(isLoading = true, message = "Loading media…")) }; runCatching { controller.loadLibrary() }.onSuccess { library -> _state.update { it.copy(media = it.media.copy(buckets = library.buckets, items = library.items, isLoading = false, message = "Loaded ${library.items.size} media files"), statusMessage = "Loaded media") } }.onFailure { error -> _state.update { it.copy(media = it.media.copy(isLoading = false, message = error.message ?: "Media load failed"), statusMessage = error.message ?: "Media load failed") } } } }
    fun selectMedia(item: MediaItem) { _state.update { it.copy(media = it.media.copy(selected = item), statusMessage = item.displayName) }; playbackController?.prepare(item) }
    fun playbackPlayPause() { playbackController?.handle(MediaPlaybackCommand(if (_state.value.playback.state == PlaybackState.Playing) MediaPlaybackCommandType.Pause else MediaPlaybackCommandType.Play)) }
    fun playbackSeekBack() { playbackController?.handle(MediaPlaybackCommand(MediaPlaybackCommandType.SeekBack)) }
    fun playbackSeekForward() { playbackController?.handle(MediaPlaybackCommand(MediaPlaybackCommandType.SeekForward)) }
    fun playbackStop() { playbackController?.handle(MediaPlaybackCommand(MediaPlaybackCommandType.Stop)) }
    override fun onCleared() { playbackController?.release(); super.onCleared() }
    fun setRemoteUrl(url: String) { _state.update { it.copy(remoteUrl = url, statusMessage = if (url.isBlank()) "Remote URL empty" else "Ready to connect") } }
    fun toggleSettingsHidden() { viewModelScope.launch { settingsUseCase.setShowHidden(!_state.value.appSettings.showHiddenFiles) } }
    fun toggleSettingsAdvanced() { viewModelScope.launch { settingsUseCase.setAdvancedMode(!_state.value.appSettings.advancedMode) } }
    fun toggleSettingsBattery() { viewModelScope.launch { settingsUseCase.setBatterySaver(!_state.value.appSettings.batterySaver) } }
    fun toggleSettingsHaptics() { viewModelScope.launch { settingsUseCase.setHaptics(!_state.value.appSettings.hapticsEnabled) } }
    fun toggleUploads() { viewModelScope.launch { settingsUseCase.updateRemoteSettings { it.copy(allowUploads = !it.allowUploads) } } }
    fun toggleDelete() { viewModelScope.launch { settingsUseCase.updateRemoteSettings { it.copy(allowDelete = !it.allowDelete) } } }
    fun toggleShowHidden() = toggleSettingsHidden()
    fun syncSettingsToWatch() { val s = _state.value.remoteSettings; val payload = WearBridgeSettingsPayload(remote = RemoteServerSettings(s.port, s.requirePin, s.allowUploads, s.allowDelete, s.autoStopMinutes, s.localNetworkOnly), advancedMode = s.advancedMode, showHiddenFiles = s.showHiddenFiles); sendWearCommand(WearBridgeCommandType.SyncSettings, "Syncing settings…", WearBridgeSettingsStringCodec.encode(payload)) }
    fun startPairing() { val bridge = wearBridgeClient ?: run { _state.update { it.copy(statusMessage = "Bridge unavailable") }; return }; viewModelScope.launch { bridge.connectedWatchNames().onSuccess { names -> _state.update { it.copy(statusMessage = if (names.isEmpty()) "No connected Wear OS watch" else "Connected: ${names.joinToString()}") } }.onFailure { error -> _state.update { it.copy(statusMessage = error.message ?: "Pairing check failed") } } } }
    fun startRemoteServer() = sendWearCommand(WearBridgeCommandType.StartWatchServer, "Starting watch server…")
    fun stopRemoteServer() = sendWearCommand(WearBridgeCommandType.StopWatchServer, "Stopping watch server…")
    fun requestWatchStatus() = sendWearCommand(WearBridgeCommandType.GetWatchServerStatus, "Requesting watch status…")
    fun onPickedFile(uri: Uri) { val controller = transferController ?: run { _state.update { it.copy(statusMessage = "Transfer unavailable") }; return }; val picked = controller.describe(uri); _state.update { it.copy(transfer = it.transfer.copy(selectedFileName = picked.displayName, message = "Selected ${picked.displayName}"), statusMessage = "Selected ${picked.displayName}") }; viewModelScope.launch { _state.update { it.copy(statusMessage = "Sending ${picked.displayName} to watch…") }; controller.sendToWatch(picked, _state.value.transfer.targetPath).onSuccess { progress -> _state.update { it.copy(transfer = it.transfer.copy(latestProgress = progress, message = progress.message), statusMessage = progress.message ?: "Transfer completed") } }.onFailure { error -> _state.update { it.copy(transfer = it.transfer.copy(message = error.message), statusMessage = error.message ?: "Transfer failed") } } } }
    fun refreshRemoteSnapshot() { val status = PhoneRemoteStatusStore.latest(); if (status == null) _state.update { it.copy(statusMessage = "No watch status snapshot yet") } else _state.update { it.copy(remoteUrl = status.url ?: it.remoteUrl, statusMessage = if (status.running) "Watch server: ${status.url} PIN ${status.pin} ${status.networkLabel.orEmpty()} ${status.batteryPercent ?: ""}%" else "Watch server stopped") } }
    fun openRemoteManager() { val url = _state.value.remoteUrl; _state.update { it.copy(statusMessage = if (url.isBlank()) "Enter watch HTTP URL first" else "Open in browser: $url") } }
    private fun sendWearCommand(type: WearBridgeCommandType, pendingMessage: String, payload: String? = null) { val bridge = wearBridgeClient; if (bridge == null) { issue(protocolClient.startRemoteServer(), "$pendingMessage Bridge unavailable"); return }; viewModelScope.launch { _state.update { it.copy(statusMessage = pendingMessage) }; bridge.sendToFirstWatch(type, payload).onSuccess { message -> _state.update { it.copy(statusMessage = message) }; delay(900); bridge.latestResultText()?.let { result -> _state.update { it.copy(statusMessage = result) } }; refreshRemoteSnapshot() }.onFailure { error -> _state.update { it.copy(statusMessage = error.message ?: "Wear command failed") } } } }
    private fun issue(command: CompanionCommand, message: String) { _state.update { it.copy(statusMessage = "$message (${command.id})") } }
}
