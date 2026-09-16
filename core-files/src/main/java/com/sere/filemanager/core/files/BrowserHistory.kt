package com.sere.filemanager.core.files

class BrowserHistory(initialPath: String = "/sdcard") {
    private val backStack = mutableListOf(initialPath)
    private val forwardStack = mutableListOf<String>()

    val current: String get() = backStack.lastOrNull() ?: "/sdcard"

    fun navigate(path: String): String {
        if (path != current) {
            backStack.add(path)
            forwardStack.clear()
        }
        return current
    }

    fun back(): String {
        if (backStack.size > 1) {
            forwardStack.add(backStack.removeAt(backStack.lastIndex))
        }
        return current
    }

    fun forward(): String {
        val next = forwardStack.removeLastOrNull() ?: return current
        backStack.add(next)
        return current
    }
}
