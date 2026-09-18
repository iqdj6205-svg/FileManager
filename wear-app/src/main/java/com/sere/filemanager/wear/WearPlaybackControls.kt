package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.CircularProgressIndicator
import com.sere.filemanager.core.media.MediaNotificationState
import com.sere.filemanager.core.media.MediaPlaybackSession
import com.sere.filemanager.core.media.PlaybackState
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearSecondaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun WearPlaybackControls(
    session: MediaPlaybackSession,
    onPlayPause: () -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
    onStop: () -> Unit,
    notification: MediaNotificationState = MediaNotificationState(),
    onBack: () -> Unit,
) {
    WearRotaryList {
        wearTitle("Player", notification.title.takeIf { notification.visible } ?: session.item?.displayName ?: "No media")
        wearInfo(notification.subtitle ?: session.state.name)
        session.item?.mimeType?.let { wearInfo(it) }
        val duration = session.durationMillis
        val position = session.positionMillis
        if (duration > 0L) {
            item { CircularProgressIndicator(progress = (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f)) }
            wearInfo("${position / 1000}s / ${duration / 1000}s")
        } else {
            wearInfo("No timeline")
        }
        if (session.item == null) wearInfo("Select audio or video first")
        wearPrimaryAction(if (session.state == PlaybackState.Playing) "Pause" else "Play", onPlayPause)
        wearSecondaryAction("-10s", onSeekBack)
        wearSecondaryAction("+10s", onSeekForward)
        wearSecondaryAction("Stop", onStop)
        wearBackAction(onBack)
    }
}
