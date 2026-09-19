package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.media.MediaNotificationState
import com.sere.filemanager.core.media.MediaPlaybackSession
import com.sere.filemanager.core.media.PlaybackState
import com.sere.filemanager.phone.ui.PhoneScreenScaffold
import com.sere.filemanager.phone.ui.PhoneSectionCard

@Composable
fun PhonePlaybackControls(
    session: MediaPlaybackSession,
    onPlayPause: () -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
    onStop: () -> Unit,
    notification: MediaNotificationState = MediaNotificationState(),
    onBack: () -> Unit,
) {
    PhoneScreenScaffold(title = "Media player", subtitle = notification.subtitle ?: "State: ${session.state}") {
        PhoneSectionCard(title = "Now playing") {
            Text(notification.title.takeIf { notification.visible } ?: session.item?.displayName ?: "No media", maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleMedium)
            Text("Position: ${session.positionMillis / 1000}s", style = MaterialTheme.typography.bodyMedium)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onSeekBack) { Text("-10s") }
            Button(onClick = onPlayPause) { Text(if (session.state == PlaybackState.Playing) "Pause" else "Play") }
            Button(onClick = onSeekForward) { Text("+10s") }
        }
        Button(onClick = onStop, modifier = Modifier.fillMaxWidth()) { Text("Stop") }
        Button(onClick = onBack) { Text("Back") }
    }
}
