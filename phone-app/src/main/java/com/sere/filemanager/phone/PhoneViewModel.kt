package com.sere.filemanager.phone

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.files.LocalFileRepository
import com.sere.filemanager.core.files.StorageAnalyzer
import com.sere.filemanager.core.files.StorageInsights
import com.sere.filemanager.core.media.MediaItem
import com.sere.filemanager.core.model.CompanionCommand
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
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
) : ViewModel() {
    private val analyzer = StorageAnalyzer(fileRepository)
    private val insights = StorageInsights()
    private val _state = MutableStateFlow(PhoneAppState())
    val state: StateFlow<PhoneAppState> = _state.asStateFlow()

    init { openPhonePath(_state.value.browser.currentPath) }

    fun openPhonePath(path: String) { viewModelScope.launch { _state.update { it.copy(browser = it.browser.copy(currentPath = path, isLoading = true, error = null)) }; runCatching { fileRepository.list(path) }.onSuccess { items -> _state.update { it.copy(browser = it.browser.copy(items = items, isLoading = false)) } }.onFailure { error -> _state.update { it.copy(browser = it.browser.copy(isLoading = false, error = error.message ?: "Cannot open folder")) } } } }
    fun analyzeCurrentPhoneFolder() { viewModelScope.launch { val path = _state.value.browser.currentPath; _state.update { it.copy(analyzer = it.analyzer.copy(isLoading = true, message = "Analyzing $path")) }; runCatching { analyzer.analyze(path) }.onSuccess { analysis -> _state.update { it.copy(analyzer = it.analyzer.copy(analysis = analysis, insights = insights.fromAnalysis(analysis), isLoading = false, message = "Analysis complete"), statusMessage = "Analysis complete") } }.onFailure { error -> _state.update { it.copy(analyzer = it.analyzer.copy(isLoading = false, message = error.message ?: "Analysis failed"), statusMessage = error.message ?: "Analysis failed") } } } }
    fun loadPhoneMedia() { val controller = mediaController ?: run { _state.update { it.copy(statusMessage = "Media unavailable") }; return }; viewModelScope.launch { _state.update { it.copy(media = it.media.copy(isLoading = true, message = "Loading media…")) }; runCatching { controller.loadLibrary() }.onSuccess { library -> _state.update { it.copy(media = it.media.copy(buckets = library.buckets, items = library.items, isLoading = false, message = "Loaded ${library.items.size} media files"), statusMessage = "Loaded media") } }.onFailure { error -> _state.update { it.copy(media = it.media.copy(isLoading = false, message = error.message ?: "Media load failed"), statusMessage = error.message ?: "Media load failed") } } } }
    fun selectMedia(item: MediaItem) { _state.update { it.copy(media = it.media.copy(selected = item), statusMessage = item.displayName) } }
    fun openPhoneItem(path: String, type: FileItemType) { if (type == FileItemType.Directory) openPhonePath(path) else selectPhoneFile(path) }
    fun phoneGoUp() { val current = _state.value.browser.currentPath.trimEnd('/'); openPhonePath(current.substringBeforeLast('/', missingDelimiterValue = "/").ifBlank { "/" }) }
    fun selectPhoneFile(path: String) { val item = _state.value.browser.items.firstOrNull { it.path == path }; _state.update { it.copy(fileActions = it.fileActions.copy(selected = item), statusMessage = item?.name ?: "No file selected") } }
    fun clearPhoneSelection() { _state.update { it.copy(fileActions = PhoneFileActionState()) } }
    fun copySelectedPhoneFile() = executePhoneFileAction("Copying…") { controller, item -> controller.copyHere(item) }
    fun deleteSelectedPhoneFile() = executePhoneFileAction("Deleting…") { controller, item -> controller.delete(item) }
    fun renameSelectedPhoneFileAsCopy() = executePhoneFileAction("Renaming…") { controller, item -> controller.renameCopy(item) }
    private fun executePhoneFileAction(pending: String, action: suspend (PhoneFileOperationsController, FileItem) -> com.sere.filemanager.core.files.FileOperationResult) { val controller = fileOperationsController ?: run { _state.update { it.copy(statusMessage = "File operations unavailable") }; return }; val item = _state.value.fileActions.selected ?: run { _state.update { it.copy(statusMessage = "No file selected") }; return }; viewModelScope.launch { _state.update { it.copy(statusMessage = pending) }; val result = action(controller, item); _state.update { it.copy(fileActions = it.fileActions.copy(message = result.message), statusMessage = result.message ?: if (result.success) "Done" else "Failed") }; openPhonePath(_state.value.browser.currentPath) } }
    fun setRemoteUrl(url: String) { _state.update { it.copy(remoteUrl = url, statusMessage = if (url.isBlank()) "Remote URL empty" else "Ready to connect") } }
    fun updateRemoteSettings(transform: (PhoneRemoteSettingsState) -> PhoneRemoteSettingsState) { _state.update { it.copy(remoteSettings = transform(it.remoteSettings)) } }
    fun toggleUploads() = updateRemoteSettings { it.copy(allowUploads = !it.allowUploads) }
    fun toggleDelete() = updateRemoteSettings { it.copy(allowDelete = !it.allowDelete) }
    fun toggleAdvancedMode() = updateRemoteSettings { it.copy(advancedMode = !it.advancedMode) }
    fun toggleShowHidden() = updateRemoteSettings { it.copy(showHiddenFiles = !it.showHiddenFiles) }
    fun syncSettingsToWatch() { val s = _state.value.remoteSettings; val payload = WearBridgeSettingsPayload(remote = com.sere.filemanager.core.model.RemoteServerSettings(s.port, s.requirePin, s.allowUploads, s.allowDelete, s.autoStopMinutes, s.localNetworkOnly), advancedMode = s.advancedMode, showHiddenFiles = s.showHiddenFiles); sendWearCommand(WearBridgeCommandType.SyncSettings, "Syncing settings…", WearBridgeSettingsStringCodec.encode(payload)) }
    fun startPairing() { val bridge = wearBridgeClient ?: run { _state.update { it.copy(statusMessage = "Bridge unavailable") }; return }; viewModelScope.launch { bridge.connectedWatchNames().onSuccess { names -> _state.update { it.copy(statusMessage = if (names.isEmpty()) "No connected Wear OS watch" else "Connected: ${names.joinToString()}") } }.onFailure { error -> _state.update { it.copy(statusMessage = error.message ?: "Pairing check failed") } } } }
    fun startRemoteServer() = sendWearCommand(WearBridgeCommandType.StartWatchServer, "Starting watch server…")
    fun stopRemoteServer() = sendWearCommand(WearBridgeCommandType.StopWatchServer, "Stopping watch server…")
    fun requestWatchStatus() = sendWearCommand(WearBridgeCommandType.GetWatchServerStatus, "Requesting watch status…")
    fun onPickedFile(uri: Uri) { val controller = transferController ?: run { _state.update { it.copy(statusMessage = "Transfer unavailable") }; return }; val picked = controller.describe(uri); _state.update { it.copy(transfer = it.transfer.copy(selectedFileName = picked.displayName, message = "Selected ${picked.displayName}"), statusMessage = "Selected ${picked.displayName}") }; viewModelScope.launch { _state.update { it.copy(statusMessage = "Sending ${picked.displayName} to watch…") }; controller.sendToWatch(picked, _state.value.transfer.targetPath).onSuccess { progress -> _state.update { it.copy(transfer = it.transfer.copy(latestProgress = progress, message = progress.message), statusMessage = progress.message ?: "Transfer completed") } }.onFailure { error -> _state.update { it.copy(transfer = it.transfer.copy(message = error.message), statusMessage = error.message ?: "Transfer failed") } } } }
    fun refreshRemoteSnapshot() { val status = PhoneRemoteStatusStore.latest(); if (status == null) _state.update { it.copy(statusMessage = "No watch status snapshot yet") } else _state.update { it.copy(remoteUrl = status.url ?: it.remoteUrl, statusMessage = if (status.running) "Watch server: ${status.url} PIN ${status.pin} ${status.networkLabel.orEmpty()} ${status.batteryPercent ?: ""}%" else "Watch server stopped") } }
    fun openRemoteManager() { val url = _state.value.remoteUrl; _state.update { it.copy(statusMessage = if (url.isBlank()) "Enter watch HTTP URL first" else "Open in browser: $url") } }
    fun sendFiles() { _state.update { it.copy(statusMessage = "Choose a file to send to watch") } }
    private fun sendWearCommand(type: WearBridgeCommandType, pendingMessage: String, payload: String? = null) { val bridge = wearBridgeClient; if (bridge == null) { issue(protocolClient.startRemoteServer(), "$pendingMessage Bridge unavailable in preview"); return }; viewModelScope.launch { _state.update { it.copy(statusMessage = pendingMessage) }; bridge.sendToFirstWatch(type, payload).onSuccess { message -> _state.update { it.copy(statusMessage = message) }; delay(900); bridge.latestResultText()?.let { result -> _state.update { it.copy(statusMessage = result) } }; refreshRemoteSnapshot() }.onFailure { error -> _state.update { it.copy(statusMessage = error.message ?: "Wear command failed") } } } }
    private fun issue(command: CompanionCommand, message: String) { _state.update { it.copy(statusMessage = "$message (${command.id})") } }
}
