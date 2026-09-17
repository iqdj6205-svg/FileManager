package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable

@Immutable
data class WearOperationState(
    val clipboard: WearClipboardState = WearClipboardState(),
    val lastMessage: String? = null,
)
