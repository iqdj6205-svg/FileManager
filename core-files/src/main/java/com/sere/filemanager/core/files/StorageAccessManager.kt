package com.sere.filemanager.core.files

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class StorageAccessManager(initialRoots: List<StorageRoot> = defaultStorageRoots()) {
    private val state = MutableStateFlow(StorageRootState(roots = initialRoots, selectedRootId = initialRoots.firstOrNull()?.id))
    val roots: Flow<StorageRootState> = state

    fun selectRoot(id: String) { state.update { current -> if (current.roots.any { it.id == id }) current.copy(selectedRootId = id, message = "Selected ${current.roots.first { it.id == id }.title}") else current.copy(message = "Root not found") } }
    fun addSafTree(title: String, uri: String) { addSafTreeAndSelect(title, uri) }
    fun addSafTreeAndSelect(title: String, uri: String): StorageRoot {
        val root = StorageRoot(id = "saf-${uri.hashCode()}", title = title, uri = uri, type = StorageRootType.SafTree, writable = true)
        state.update { current -> current.copy(roots = current.roots.filterNot { it.id == root.id } + root, selectedRootId = root.id, message = "Added $title") }
        return root
    }
    fun selectedPathOrDefault(defaultPath: String = "/sdcard/Download"): String = state.value.roots.firstOrNull { it.id == state.value.selectedRootId }?.path ?: defaultPath
}
