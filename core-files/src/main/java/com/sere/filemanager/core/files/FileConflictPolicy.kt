package com.sere.filemanager.core.files

import java.io.File

enum class FileConflictStrategy { KeepBoth, Replace, Skip, Fail }

data class FileConflictDecision(
    val targetPath: String,
    val shouldWrite: Boolean,
    val shouldReplace: Boolean = false,
    val message: String? = null,
)

class FileConflictResolver(
    private val strategy: FileConflictStrategy = FileConflictStrategy.KeepBoth,
) {
    fun resolve(targetPath: String): FileConflictDecision {
        val target = File(targetPath)
        if (!target.exists()) return FileConflictDecision(targetPath, shouldWrite = true)
        return when (strategy) {
            FileConflictStrategy.KeepBoth -> FileConflictDecision(nextAvailablePath(targetPath), shouldWrite = true, message = "Renamed to avoid conflict")
            FileConflictStrategy.Replace -> FileConflictDecision(targetPath, shouldWrite = true, shouldReplace = true, message = "Replacing existing file")
            FileConflictStrategy.Skip -> FileConflictDecision(targetPath, shouldWrite = false, message = "Skipped existing file")
            FileConflictStrategy.Fail -> FileConflictDecision(targetPath, shouldWrite = false, message = "Target already exists")
        }
    }

    fun nextAvailablePath(path: String): String {
        val file = File(path)
        val parent = file.parentFile ?: File("/")
        val name = file.name
        val dot = name.lastIndexOf('.')
        val base = if (dot > 0) name.substring(0, dot) else name
        val ext = if (dot > 0) name.substring(dot) else ""
        var index = 1
        while (true) {
            val candidate = File(parent, "$base ($index)$ext")
            if (!candidate.exists()) return candidate.absolutePath
            index++
        }
    }
}
