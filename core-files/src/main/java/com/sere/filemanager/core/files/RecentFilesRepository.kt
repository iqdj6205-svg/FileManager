package com.sere.filemanager.core.files

interface RecentFilesRepository {
    fun list(): List<String>
    fun markOpened(path: String)
    fun clear()
}

class InMemoryRecentFilesRepository(
    private val maxItems: Int = 30,
) : RecentFilesRepository {
    private val items = mutableListOf<String>()

    override fun list(): List<String> = items.toList()

    override fun markOpened(path: String) {
        items.remove(path)
        items.add(0, path)
        while (items.size > maxItems) items.removeAt(items.lastIndex)
    }

    override fun clear() { items.clear() }
}
