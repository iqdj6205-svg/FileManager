package com.sere.filemanager.core.files

import android.net.Uri

class StorageRootResolver {
    fun canUsePath(root: StorageRoot): Boolean = !root.path.isNullOrBlank()
    fun canUseSaf(root: StorageRoot): Boolean = root.type == StorageRootType.SafTree && !root.uri.isNullOrBlank()
    fun safUri(root: StorageRoot): Uri? = root.uri?.let { runCatching { Uri.parse(it) }.getOrNull() }
    fun preferredDisplayPath(root: StorageRoot): String = when {
        !root.path.isNullOrBlank() -> root.path
        root.type == StorageRootType.SafTree -> "Selected folder access"
        root.type == StorageRootType.MediaStore -> "Android media library"
        root.type == StorageRootType.AdvancedAdb -> "ADB/root access"
        else -> root.title
    }
}
