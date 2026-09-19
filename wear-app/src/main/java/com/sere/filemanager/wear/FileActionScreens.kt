package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearSecondaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun FileActionSheet(
    item: FileItem,
    onDetails: () -> Unit,
    onRename: () -> Unit,
    onCopy: () -> Unit,
    onMove: () -> Unit,
    onDelete: () -> Unit,
    onFavorite: () -> Unit,
    onBack: () -> Unit,
) {
    WearRotaryList {
        wearTitle(item.name)
        wearPrimaryAction("Details", onDetails)
        wearPrimaryAction("Rename", onRename)
        wearPrimaryAction("Copy", onCopy)
        wearPrimaryAction("Move", onMove)
        wearPrimaryAction("Favorite", onFavorite)
        wearSecondaryAction("Delete", onDelete)
        wearBackAction(onBack)
    }
}

@Composable
fun FileDetailsScreen(item: FileItem, size: String, onBack: () -> Unit) {
    WearRotaryList {
        wearTitle("Details", item.name)
        wearInfo(item.type.name)
        wearInfo(size.ifBlank { "No size" })
        wearInfo(item.path)
        wearBackAction(onBack)
    }
}

@Composable
fun ConfirmDeleteScreen(fileName: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
    WearRotaryList {
        wearTitle("Delete?", fileName)
        wearInfo("This cannot be undone.")
        wearPrimaryAction("Delete", onConfirm)
        wearBackAction(onCancel, "Cancel")
    }
}
