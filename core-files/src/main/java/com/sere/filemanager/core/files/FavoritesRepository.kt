package com.sere.filemanager.core.files

interface FavoritesRepository {
    fun list(): Set<String>
    fun isFavorite(path: String): Boolean
    fun toggle(path: String): Set<String>
}

class InMemoryFavoritesRepository : FavoritesRepository {
    private val favorites = linkedSetOf<String>()

    override fun list(): Set<String> = favorites.toSet()
    override fun isFavorite(path: String): Boolean = favorites.contains(path)

    override fun toggle(path: String): Set<String> {
        if (!favorites.add(path)) favorites.remove(path)
        return list()
    }
}
