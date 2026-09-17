package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItemType

data class StorageRoot(
    val id: String,
    val title: String,
    val path: String? = null,
    val uri: String? = null,
    val type: StorageRootType,
    val writable: Boolean = false,
)

enum class StorageRootType { AppPrivate, SharedStorage, MediaStore, SafTree, Downloads, Documents, Pictures, Music, Videos, AdvancedAdb }

data class StorageRootState(
    val roots: List<StorageRoot> = emptyList(),
    val selectedRootId: String? = null,
    val message: String? = null,
)

fun defaultStorageRoots(): List<StorageRoot> = listOf(
    StorageRoot("downloads", "Downloads", "/sdcard/Download", type = StorageRootType.Downloads, writable = true),
    StorageRoot("documents", "Documents", "/sdcard/Documents", type = StorageRootType.Documents, writable = true),
    StorageRoot("pictures", "Pictures", "/sdcard/Pictures", type = StorageRootType.Pictures, writable = true),
    StorageRoot("music", "Music", "/sdcard/Music", type = StorageRootType.Music, writable = true),
    StorageRoot("videos", "Videos", "/sdcard/Movies", type = StorageRootType.Videos, writable = true),
    StorageRoot("media", "Media library", type = StorageRootType.MediaStore),
    StorageRoot("advanced", "ADB full storage", "/", type = StorageRootType.AdvancedAdb),
)
