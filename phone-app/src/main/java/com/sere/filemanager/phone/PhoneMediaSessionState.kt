package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.media.MediaNotificationState
import com.sere.filemanager.core.media.MediaSessionMetadata

@Immutable
data class PhoneMediaSessionState(
    val metadata: MediaSessionMetadata? = null,
    val notification: MediaNotificationState = MediaNotificationState(),
)
