package com.sere.filemanager.core.files

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class StorageAccessManager(initialRoots: List<StorageRoot> = defaultStorageRoots()) {
    private val state = MutableStateFlow(StorageRootState(roots = initialRoots, selectedRootId = initialRoots.firstOrNull()?.id))
    val roots: Flow<StorageRootState> = state

    fun selectRoot(id: String) { state.update { current -> if (current.roots.any { it.id == id }) current.copy(selectedRootId = id, message = "Selected ${current.roots.first { it.id == id }.title}") else current.copy(message = "Root not found") } }
    fun addSafTree(title: String, uri: String) { state.update { current -> current.copy(roots = current.roots + StorageRoot(id = "saf-${uri.hashCode()}", title = title, uri = uri, type = StorageRootType.SafTree, writable = true), message = "Added $title") } }
    fun selectedPathOrDefault(defaultPath: String = "/sdcard/Download"): String = state.value.roots.firstOrNull { it.id == state.value.selectedRootId }?.path ?: defaultPath
}
