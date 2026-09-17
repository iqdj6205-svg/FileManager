package com.sere.filemanager.core.media

enum class PlaybackState { Idle, Preparing, Playing, Paused, Completed, Error }

data class MediaPlaybackSession(
    val item: MediaItem? = null,
    val state: PlaybackState = PlaybackState.Idle,
    val positionMillis: Long = 0L,
    val durationMillis: Long? = null,
    val errorMessage: String? = null,
) {
    val canPlay: Boolean get() = item != null && state != PlaybackState.Preparing
    val isActive: Boolean get() = item != null && state != PlaybackState.Idle
}

data class MediaPlaybackCommand(
    val type: MediaPlaybackCommandType,
    val seekToMillis: Long? = null,
)

enum class MediaPlaybackCommandType { Play, Pause, Stop, SeekForward, SeekBack, SeekTo }
