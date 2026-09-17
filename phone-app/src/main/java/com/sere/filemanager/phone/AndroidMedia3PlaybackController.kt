package com.sere.filemanager.phone

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.sere.filemanager.core.media.MediaItem
import com.sere.filemanager.core.media.MediaPlaybackCommand
import com.sere.filemanager.core.media.MediaPlaybackCommandType
import com.sere.filemanager.core.media.MediaPlaybackController
import com.sere.filemanager.core.media.MediaPlaybackSession
import com.sere.filemanager.core.media.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AndroidMedia3PlaybackController(context: Context) : MediaPlaybackController {
    private val player = ExoPlayer.Builder(context.applicationContext).build()
    private val _session = MutableStateFlow(MediaPlaybackSession())
    override val session: StateFlow<MediaPlaybackSession> = _session

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                val mapped = when (playbackState) {
                    Player.STATE_BUFFERING -> PlaybackState.Preparing
                    Player.STATE_ENDED -> PlaybackState.Completed
                    Player.STATE_READY -> if (player.isPlaying) PlaybackState.Playing else PlaybackState.Paused
                    else -> _session.value.state
                }
                updateState(mapped)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateState(if (isPlaying) PlaybackState.Playing else PlaybackState.Paused)
            }
        })
    }

    override fun prepare(item: MediaItem) {
        _session.value = MediaPlaybackSession(item = item, state = PlaybackState.Preparing)
        runCatching {
            player.setMediaItem(ExoMediaItem.fromUri(item.uri))
            player.prepare()
            _session.value = MediaPlaybackSession(item = item, state = PlaybackState.Paused, durationMillis = player.duration.takeIf { it > 0 })
        }.onFailure { error ->
            _session.value = MediaPlaybackSession(item = item, state = PlaybackState.Error, errorMessage = error.message)
        }
    }

    override fun handle(command: MediaPlaybackCommand) {
        when (command.type) {
            MediaPlaybackCommandType.Play -> { player.play(); updateState(PlaybackState.Playing) }
            MediaPlaybackCommandType.Pause -> { player.pause(); updateState(PlaybackState.Paused) }
            MediaPlaybackCommandType.Stop -> { player.stop(); _session.value = MediaPlaybackSession() }
            MediaPlaybackCommandType.SeekForward -> { player.seekTo(player.currentPosition + 10_000L); updatePosition() }
            MediaPlaybackCommandType.SeekBack -> { player.seekTo((player.currentPosition - 10_000L).coerceAtLeast(0L)); updatePosition() }
            MediaPlaybackCommandType.SeekTo -> { player.seekTo(command.seekToMillis ?: player.currentPosition); updatePosition() }
        }
    }

    private fun updateState(state: PlaybackState) {
        _session.update { it.copy(state = state, positionMillis = player.currentPosition, durationMillis = player.duration.takeIf { d -> d > 0 }) }
    }

    private fun updatePosition() {
        _session.update { it.copy(positionMillis = player.currentPosition, durationMillis = player.duration.takeIf { d -> d > 0 }) }
    }

    override fun release() { player.release(); _session.value = MediaPlaybackSession() }
}
