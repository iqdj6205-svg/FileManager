package com.sere.filemanager.core.media

class MediaSearch {
    fun filter(items: List<MediaItem>, query: String): List<MediaItem> {
        if (query.isBlank()) return items
        return items.filter { it.title.contains(query, ignoreCase = true) || it.path.contains(query, ignoreCase = true) }
    }
}
