package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.media.MediaPlaybackSession
import com.sere.filemanager.core.media.PlaybackState

@Composable
fun PhonePlaybackControls(
    session: MediaPlaybackSession,
    onPlayPause: () -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
    onStop: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Media player", style = MaterialTheme.typography.headlineSmall)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(session.item?.displayName ?: "No media", maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("State: ${session.state}")
                Text("Position: ${session.positionMillis / 1000}s")
            }
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
