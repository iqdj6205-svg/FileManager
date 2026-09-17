package com.sere.filemanager.core.model

data class FileManagerHomeAction(
    val id: String,
    val label: String,
    val emoji: String,
    val description: String,
)

object DefaultHomeActions {
    val files = FileManagerHomeAction("files", "Files", "📁", "Browse storage")
    val media = FileManagerHomeAction("media", "Media", "🖼", "Images, audio and video")
    val remote = FileManagerHomeAction("remote", "Remote", "🌐", "HTTP access and sharing")
    val analyzer = FileManagerHomeAction("analyzer", "Analyzer", "📊", "Find what uses space")
    val settings = FileManagerHomeAction("settings", "Settings", "⚙️", "Permissions and preferences")
    val advanced = FileManagerHomeAction("advanced", "Advanced", "🛠", "ADB and diagnostics")
}
