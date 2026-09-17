package com.sere.filemanager.core.media

data class MediaSessionMetadata(
    val title: String,
    val artist: String? = null,
    val album: String? = null,
    val artworkUri: String? = null,
    val durationMillis: Long? = null,
)

data class MediaNotificationState(
    val visible: Boolean = false,
    val title: String = "FileManager",
    val subtitle: String? = null,
    val isPlaying: Boolean = false,
    val canSeek: Boolean = true,
)

class MediaSessionStateMapper {
    fun metadata(item: MediaItem): MediaSessionMetadata = MediaSessionMetadata(
        title = item.displayName,
        artist = item.bucketName,
        artworkUri = item.uri,
        durationMillis = item.durationMillis,
    )

    fun notification(session: MediaPlaybackSession): MediaNotificationState {
        val item = session.item ?: return MediaNotificationState(visible = false)
        return MediaNotificationState(
            visible = session.state != PlaybackState.Idle,
            title = item.displayName,
            subtitle = session.state.name,
            isPlaying = session.state == PlaybackState.Playing,
            canSeek = session.durationMillis != null,
        )
    }
}
