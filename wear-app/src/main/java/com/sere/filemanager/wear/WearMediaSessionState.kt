package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.media.MediaNotificationState
import com.sere.filemanager.core.media.MediaSessionMetadata

@Immutable
data class WearMediaSessionState(
    val metadata: MediaSessionMetadata? = null,
    val notification: MediaNotificationState = MediaNotificationState(),
)
