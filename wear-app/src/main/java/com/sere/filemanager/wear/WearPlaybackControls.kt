package com.sere.filemanager.wear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.media.MediaPlaybackSession
import com.sere.filemanager.core.media.MediaNotificationState
import com.sere.filemanager.core.media.PlaybackState

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
    ScalingLazyColumn(modifier = Modifier.fillMaxSize().padding(10.dp), contentPadding = PaddingValues(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        item { Text("Player", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
        item { Text(notification.title.takeIf { notification.visible } ?: session.item?.displayName ?: "No media", maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center) }
        item { Text(notification.subtitle ?: session.state.name, textAlign = TextAlign.Center) }
        item { Chip(label = { Text(if (session.state == PlaybackState.Playing) "Pause" else "Play") }, onClick = onPlayPause, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("-10s") }, onClick = onSeekBack, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("+10s") }, onClick = onSeekForward, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Stop") }, onClick = onStop, modifier = Modifier.fillMaxWidth()) }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
    }
}
