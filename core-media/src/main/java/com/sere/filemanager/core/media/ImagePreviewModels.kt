package com.sere.filemanager.core.media

data class ImagePreviewState(
    val uri: String? = null,
    val title: String? = null,
    val isZoomed: Boolean = false,
    val rotationDegrees: Float = 0f,
    val message: String? = null,
)

enum class ImagePreviewAction { ZoomToggle, RotateLeft, RotateRight, Share, Details }

class ImagePreviewController {
    fun open(item: MediaItem): ImagePreviewState = ImagePreviewState(uri = item.uri, title = item.displayName)
    fun reduce(state: ImagePreviewState, action: ImagePreviewAction): ImagePreviewState = when (action) {
        ImagePreviewAction.ZoomToggle -> state.copy(isZoomed = !state.isZoomed)
        ImagePreviewAction.RotateLeft -> state.copy(rotationDegrees = state.rotationDegrees - 90f)
        ImagePreviewAction.RotateRight -> state.copy(rotationDegrees = state.rotationDegrees + 90f)
        ImagePreviewAction.Share -> state.copy(message = "Share action queued")
        ImagePreviewAction.Details -> state.copy(message = "Image details queued")
    }
}
