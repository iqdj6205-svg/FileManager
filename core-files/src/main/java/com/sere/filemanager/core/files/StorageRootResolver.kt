package com.sere.filemanager.core.files

import android.net.Uri

class StorageRootResolver {
    fun canUsePath(root: StorageRoot): Boolean = !root.path.isNullOrBlank()
    fun canUseSaf(root: StorageRoot): Boolean = root.type == StorageRootType.SafTree && !root.uri.isNullOrBlank()
    fun safUri(root: StorageRoot): Uri? = root.uri?.let { runCatching { Uri.parse(it) }.getOrNull() }
    fun preferredDisplayPath(root: StorageRoot): String = root.path ?: root.uri ?: root.title
}
