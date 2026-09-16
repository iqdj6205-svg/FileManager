package com.sere.filemanager.wear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.files.InMemoryFavoritesRepository
import com.sere.filemanager.core.files.LocalFileRepository
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
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
) : ViewModel() {
    private val _state = MutableStateFlow(WearAppState())
    val state: StateFlow<WearAppState> = _state.asStateFlow()

    init { openPath(_state.value.browser.currentPath) }

    fun openPath(path: String) {
        viewModelScope.launch {
            _state.update { it.copy(browser = it.browser.copy(currentPath = path, isLoading = true, error = null)) }
            runCatching { fileRepository.list(path) }
                .onSuccess { items -> _state.update { it.copy(browser = it.browser.copy(items = items, isLoading = false)) } }
                .onFailure { error -> _state.update { it.copy(browser = it.browser.copy(isLoading = false, error = error.message ?: "Cannot open path")) } }
        }
    }

    fun openItem(path: String, type: FileItemType) {
        if (type == FileItemType.Directory) openPath(path) else selectItem(path)
    }

    fun goUp() {
        val current = _state.value.browser.currentPath.trimEnd('/')
        val parent = current.substringBeforeLast('/', missingDelimiterValue = "/").ifBlank { "/" }
        openPath(parent)
    }

    fun selectItem(path: String?) {
        _state.update { it.copy(browser = it.browser.copy(selectedPath = path)) }
    }

    fun selectedItem(): FileItem? {
        val selected = _state.value.browser.selectedPath ?: return null
        return _state.value.browser.items.firstOrNull { it.path == selected }
    }

    fun toggleFavoriteSelected() {
        val item = selectedItem() ?: return
        favoritesRepository.toggle(item.path)
        _state.update { it.copy(operation = it.operation.copy(message = "Favorite updated"), browser = it.browser.copy(selectedPath = null)) }
    }

    fun copySelected() {
        val item = selectedItem() ?: return
        execute(BrowserController.copyOperation(item.path))
    }

    fun deleteSelected() {
        val item = selectedItem() ?: return
        execute(BrowserController.deleteOperation(item.path))
    }

    fun renameSelected(newName: String) {
        val item = selectedItem() ?: return
        execute(BrowserController.renameOperation(item.path, newName))
    }

    fun createQuickFolder(name: String = "New folder") {
        execute(BrowserController.createFolderOperation(_state.value.browser.currentPath, name))
    }

    fun onPermissionsResult(results: Map<String, Boolean>) {
        val granted = results.values.count { it }
        val total = results.size
        _state.update { it.copy(operation = it.operation.copy(message = if (total == 0) "No permission needed" else "Permissions: $granted/$total granted")) }
    }

    private fun execute(operation: com.sere.filemanager.core.files.FileOperation) {
        viewModelScope.launch {
            _state.update { it.copy(operation = it.operation.copy(inProgress = true, message = null)) }
            val result = safeOperations.execute(operation)
            _state.update { it.copy(operation = it.operation.copy(inProgress = false, message = ActionMessages.from(result)), browser = it.browser.copy(selectedPath = null)) }
            openPath(_state.value.browser.currentPath)
        }
    }

    fun clearMessage() {
        _state.update { it.copy(operation = it.operation.copy(message = null)) }
    }

    fun startRemoteServer() {
        viewModelScope.launch {
            val session = remoteController.start()
            _state.update { it.copy(remoteSession = session) }
        }
    }

    fun stopRemoteServer() {
        viewModelScope.launch {
            val session = remoteController.stop()
            _state.update { it.copy(remoteSession = session) }
        }
    }

    fun setAdvancedMode(enabled: Boolean) { _state.update { it.copy(advancedModeEnabled = enabled) } }
    fun setBatterySaver(enabled: Boolean) { _state.update { it.copy(batterySaverEnabled = enabled) } }
}
