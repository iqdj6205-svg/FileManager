package com.sere.filemanager.core.media

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class InMemoryMediaPlaybackController : MediaPlaybackController {
    private val _session = MutableStateFlow(MediaPlaybackSession())
    override val session: StateFlow<MediaPlaybackSession> = _session

    override fun prepare(item: MediaItem) {
        _session.value = MediaPlaybackSession(item = item, state = PlaybackState.Paused)
    }

    override fun handle(command: MediaPlaybackCommand) {
        _session.update { current ->
            when (command.type) {
                MediaPlaybackCommandType.Play -> current.copy(state = PlaybackState.Playing)
                MediaPlaybackCommandType.Pause -> current.copy(state = PlaybackState.Paused)
                MediaPlaybackCommandType.Stop -> MediaPlaybackSession()
                MediaPlaybackCommandType.SeekForward -> current.copy(positionMillis = current.positionMillis + 10_000L)
                MediaPlaybackCommandType.SeekBack -> current.copy(positionMillis = (current.positionMillis - 10_000L).coerceAtLeast(0L))
                MediaPlaybackCommandType.SeekTo -> current.copy(positionMillis = command.seekToMillis ?: current.positionMillis)
            }
        }
    }

    override fun release() { _session.value = MediaPlaybackSession() }
}
