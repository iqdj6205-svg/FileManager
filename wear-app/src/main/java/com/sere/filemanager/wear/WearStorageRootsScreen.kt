package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import com.sere.filemanager.core.files.StorageRoot
import com.sere.filemanager.core.files.StorageRootState
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun WearStorageRootsScreen(
    state: StorageRootState,
    onSelect: (StorageRoot) -> Unit,
    onBack: () -> Unit,
) {
    WearRotaryList {
        wearTitle("Storage", state.message)
        if (state.roots.isEmpty()) wearInfo("No storage roots available")
        state.roots.forEach { root ->
            val selected = root.id == state.selectedRootId
            val label = if (selected) "✓ ${root.title}" else root.title
            val subtitle = root.path ?: root.uri ?: root.type.name
            wearPrimaryAction(label) { onSelect(root) }
            wearInfo(subtitle)
        }
        wearBackAction(onBack)
    }
}
