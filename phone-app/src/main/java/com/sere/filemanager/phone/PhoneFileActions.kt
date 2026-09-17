package com.sere.filemanager.phone

import com.sere.filemanager.core.model.FileItem

data class PhoneFileActionState(
    val selected: FileItem? = null,
    val message: String? = null,
)

enum class PhoneFileAction { Open, Details, Rename, Copy, Move, Delete, Share, Favorite }
