package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem
import java.io.File

class RecursiveScanner(
    private val maxFiles: Int = 1_000,
) {
    fun scan(rootPath: String, shouldContinue: () -> Boolean = { true }): List<FileItem> {
        val result = mutableListOf<FileItem>()
        fun walk(file: File) {
            if (!shouldContinue() || result.size >= maxFiles) return
            if (!file.exists()) return
            file.listFiles()?.forEach { child ->
                if (result.size >= maxFiles || !shouldContinue()) return
                result.add(LocalFileRepository(listOf(file)).run { childToItem(child) })
                if (child.isDirectory) walk(child)
            }
        }
        walk(File(rootPath))
        return result
    }
}
