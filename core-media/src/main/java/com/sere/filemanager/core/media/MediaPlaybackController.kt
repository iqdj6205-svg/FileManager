package com.sere.filemanager.core.media

import kotlinx.coroutines.flow.StateFlow

interface MediaPlaybackController {
    val session: StateFlow<MediaPlaybackSession>
    fun prepare(item: MediaItem)
    fun handle(command: MediaPlaybackCommand)
    fun release()
}
