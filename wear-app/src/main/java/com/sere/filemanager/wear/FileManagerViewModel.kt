package com.sere.filemanager.wear

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sere.filemanager.core.files.FileClipboardMode
import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.files.InMemoryFavoritesRepository
import com.sere.filemanager.core.files.LocalFileRepository
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.media.ImagePreviewAction
import com.sere.filemanager.core.media.ImagePreviewController
import com.sere.filemanager.core.media.MediaItem
import com.sere.filemanager.core.media.MediaPlaybackCommand
import com.sere.filemanager.core.media.MediaPlaybackCommandType
import com.sere.filemanager.core.media.MediaPlaybackController
import com.sere.filemanager.core.media.MediaSessionStateMapper
import com.sere.filemanager.core.media.PlaybackState
import com.sere.filemanager.core.model.AppPermissionState
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import com.sere.filemanager.core.model.PermissionStatus
import com.sere.filemanager.core.remote.InMemoryRemoteServerController
import com.sere.filemanager.core.remote.RemoteServerController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FileManagerViewModel(
    private val fileRepository: FileRepository = LocalFileRepository(),
    private val remoteController: RemoteServerController = InMemoryRemoteServerController(),
    private val safeOperations: SafeFileOperations = SafeFileOperations(fileRepository),
    private val favoritesRepository: InMemoryFavoritesRepository = InMemoryFavoritesRepository(),
    private val mediaController: WearMediaController? = null,
    private val playbackController: MediaPlaybackController? = null,
) : ViewModel() {
    private val wearActions = WearFileManagerActions(safeOperations)
    private val imagePreviewController = ImagePreviewController()
    private val sessionMapper = MediaSessionStateMapper()
    private val _state = MutableStateFlow(WearAppState())
    val state: StateFlow<WearAppState> = _state.asStateFlow()

    init {
        openPath(_state.value.browser.currentPath)
        playbackController?.let { controller ->
            viewModelScope.launch {
                controller.session.collect { session ->
                    _state.update {
                        it.copy(
                            playback = session,
                            mediaSession = WearMediaSessionState(
                                metadata = session.item?.let(sessionMapper::metadata),
                                notification = sessionMapper.notification(session),
                            ),
                        )
                    }
                }
            }
        }
    }

    fun openImagePreview(item: MediaItem) { _state.update { it.copy(media = it.media.copy(selected = item), imagePreview = imagePreviewController.open(item)) } }
    fun imageZoomToggle() { _state.update { it.copy(imagePreview = imagePreviewController.reduce(it.imagePreview, ImagePreviewAction.ZoomToggle)) } }
    fun imageRotateLeft() { _state.update { it.copy(imagePreview = imagePreviewController.reduce(it.imagePreview, ImagePreviewAction.RotateLeft)) } }
    fun imageRotateRight() { _state.update { it.copy(imagePreview = imagePreviewController.reduce(it.imagePreview, ImagePreviewAction.RotateRight)) } }

    fun loadWearMedia() { val controller = mediaController ?: run { _state.update { it.copy(media = it.media.copy(message = "Media unavailable")) }; return }; viewModelScope.launch { _state.update { it.copy(media = it.media.copy(isLoading = true, message = "Loading media…")) }; runCatching { controller.load() }.onSuccess { library -> _state.update { it.copy(media = it.media.copy(images = library.items.filter { item -> item.mimeType?.startsWith("image/") == true }, audio = library.items.filter { item -> item.mimeType?.startsWith("audio/") == true }, video = library.items.filter { item -> item.mimeType?.startsWith("video/") == true }, isLoading = false, message = "Loaded ${library.items.size}")) } }.onFailure { error -> _state.update { it.copy(media = it.media.copy(isLoading = false, message = error.message ?: "Media load failed")) } } } }
    fun selectWearMedia(item: MediaItem) { _state.update { it.copy(media = it.media.copy(selected = item)) }; playbackController?.prepare(item) }
    fun playbackPlayPause() { playbackController?.handle(MediaPlaybackCommand(if (_state.value.playback.state == PlaybackState.Playing) MediaPlaybackCommandType.Pause else MediaPlaybackCommandType.Play)) }
    fun playbackSeekBack() { playbackController?.handle(MediaPlaybackCommand(MediaPlaybackCommandType.SeekBack)) }
    fun playbackSeekForward() { playbackController?.handle(MediaPlaybackCommand(MediaPlaybackCommandType.SeekForward)) }
    fun playbackStop() { playbackController?.handle(MediaPlaybackCommand(MediaPlaybackCommandType.Stop)) }
    fun openPath(path: String) { viewModelScope.launch { _state.update { it.copy(browser = it.browser.copy(currentPath = path, isLoading = true, error = null)) }; runCatching { fileRepository.list(path) }.onSuccess { items -> _state.update { it.copy(browser = it.browser.copy(items = items, isLoading = false)) } }.onFailure { error -> _state.update { it.copy(browser = it.browser.copy(isLoading = false, error = error.message ?: "Cannot open path")) } } } }
    fun openItem(path: String, type: FileItemType) { if (type == FileItemType.Directory) openPath(path) else selectItem(path) }
    fun goUp() { val current = _state.value.browser.currentPath.trimEnd('/'); openPath(current.substringBeforeLast('/', missingDelimiterValue = "/").ifBlank { "/" }) }
    fun selectItem(path: String?) { _state.update { it.copy(browser = it.browser.copy(selectedPath = path)) } }
    fun selectedItem(): FileItem? { val selected = _state.value.browser.selectedPath ?: return null; return _state.value.browser.items.firstOrNull { it.path == selected } }
    fun markSelectedForCopy() = markSelected(FileClipboardMode.Copy)
    fun markSelectedForMove() = markSelected(FileClipboardMode.Move)
    private fun markSelected(mode: FileClipboardMode) { val item = selectedItem() ?: return; val clip = wearActions.mark(item, mode); _state.update { it.copy(wearOperations = it.wearOperations.copy(clipboard = clip, lastMessage = clip.message), operation = it.operation.copy(message = clip.message), browser = it.browser.copy(selectedPath = null)) } }
    fun pasteClipboardHere() { val target = _state.value.browser.currentPath; viewModelScope.launch { val (result, clip) = wearActions.pasteInto(target); _state.update { it.copy(wearOperations = it.wearOperations.copy(clipboard = clip, lastMessage = result.message), operation = it.operation.copy(message = result.message ?: if (result.success) "Paste complete" else "Paste failed")) }; openPath(target) } }
    fun clearClipboard() { val clip = wearActions.clear(); _state.update { it.copy(wearOperations = it.wearOperations.copy(clipboard = clip, lastMessage = clip.message), operation = it.operation.copy(message = clip.message)) } }
    fun toggleFavoriteSelected() { val item = selectedItem() ?: return; favoritesRepository.toggle(item.path); _state.update { it.copy(operation = it.operation.copy(message = "Favorite updated"), browser = it.browser.copy(selectedPath = null)) } }
    fun copySelected() { val item = selectedItem() ?: return; execute(BrowserController.copyOperation(item.path)) }
    fun duplicateSelected() { val item = selectedItem() ?: return; viewModelScope.launch { val result = wearActions.duplicate(item); _state.update { it.copy(operation = it.operation.copy(message = result.message ?: if (result.success) "Duplicated" else "Duplicate failed"), browser = it.browser.copy(selectedPath = null)) }; openPath(_state.value.browser.currentPath) } }
    fun deleteSelected() { val item = selectedItem() ?: return; execute(BrowserController.deleteOperation(item.path)) }
    fun renameSelected(newName: String) { val item = selectedItem() ?: return; execute(BrowserController.renameOperation(item.path, newName)) }
    fun createQuickFolder(name: String = "New folder") { execute(BrowserController.createFolderOperation(_state.value.browser.currentPath, name)) }
    fun onPermissionsResult(results: Map<String, Boolean>) { val granted = results.values.count { it }; val total = results.size; val message = if (total == 0) "No permission needed" else "Permissions: $granted/$total granted"; val status = PermissionStatus(mediaImages = stateFor(results, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE), mediaVideo = stateFor(results, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_EXTERNAL_STORAGE), mediaAudio = stateFor(results, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE), notifications = stateFor(results, Manifest.permission.POST_NOTIFICATIONS)); _state.update { it.copy(permissions = it.permissions.copy(status = status, lastRequestMessage = message), operation = it.operation.copy(message = message)) } }
    private fun stateFor(results: Map<String, Boolean>, permission: String, legacy: String? = null): AppPermissionState = when (results[permission] ?: (if (legacy != null) results[legacy] else null)) { true -> AppPermissionState.Granted; false -> AppPermissionState.Denied; null -> AppPermissionState.Unknown }
    private fun execute(operation: com.sere.filemanager.core.files.FileOperation) { viewModelScope.launch { _state.update { it.copy(operation = it.operation.copy(inProgress = true, message = null)) }; val result = safeOperations.execute(operation); _state.update { it.copy(operation = it.operation.copy(inProgress = false, message = ActionMessages.from(result)), browser = it.browser.copy(selectedPath = null)) }; openPath(_state.value.browser.currentPath) } }
    fun clearMessage() { _state.update { it.copy(operation = it.operation.copy(message = null)) } }
    fun startRemoteServer() { viewModelScope.launch { val session = remoteController.start(); _state.update { it.copy(remoteSession = session) } } }
    fun stopRemoteServer() { viewModelScope.launch { val session = remoteController.stop(); _state.update { it.copy(remoteSession = session) } } }
    fun setAdvancedMode(enabled: Boolean) { _state.update { it.copy(advancedModeEnabled = enabled) } }
    fun setBatterySaver(enabled: Boolean) { _state.update { it.copy(batterySaverEnabled = enabled) } }
    override fun onCleared() { playbackController?.release(); super.onCleared() }
}
