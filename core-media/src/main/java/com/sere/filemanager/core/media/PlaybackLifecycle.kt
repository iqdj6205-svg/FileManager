package com.sere.filemanager.core.media

class PlaybackLifecycle(private val controller: MediaPlaybackController) {
    fun pauseForBackground() {
        controller.handle(MediaPlaybackCommand(MediaPlaybackCommandType.Pause))
    }

    fun stopAndRelease() {
        controller.handle(MediaPlaybackCommand(MediaPlaybackCommandType.Stop))
        controller.release()
    }
}
