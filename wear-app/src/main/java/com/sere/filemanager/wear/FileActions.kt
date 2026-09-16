package com.sere.filemanager.wear

import com.sere.filemanager.core.model.FileItem

enum class FileAction { Open, Share, Rename, Copy, Move, Delete, Details, Favorite }

data class FileActionUiState(
    val target: FileItem? = null,
    val availableActions: List<FileAction> = emptyList(),
)

fun actionsFor(item: FileItem): List<FileAction> = listOf(
    FileAction.Open,
    FileAction.Share,
    FileAction.Rename,
    FileAction.Copy,
    FileAction.Move,
    FileAction.Details,
    FileAction.Favorite,
    FileAction.Delete,
)
