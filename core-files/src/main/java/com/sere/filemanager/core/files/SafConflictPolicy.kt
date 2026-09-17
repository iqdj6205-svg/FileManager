package com.sere.filemanager.core.files

import androidx.documentfile.provider.DocumentFile

class SafConflictResolver(
    private val strategy: FileConflictStrategy = FileConflictStrategy.KeepBoth,
) {
    fun resolve(parent: DocumentFile, desiredName: String): SafConflictDecision {
        val clean = OperationNamePolicy.sanitizeInputName(desiredName)
        val existing = parent.findFile(clean)
        if (existing == null) return SafConflictDecision(clean, shouldWrite = true)
        return when (strategy) {
            FileConflictStrategy.KeepBoth -> SafConflictDecision(nextAvailableName(parent, clean), shouldWrite = true, message = "Renamed to avoid conflict")
            FileConflictStrategy.Replace -> SafConflictDecision(clean, shouldWrite = true, shouldReplace = true, existing = existing, message = "Replacing existing file")
            FileConflictStrategy.Skip -> SafConflictDecision(clean, shouldWrite = false, message = "Skipped existing file")
            FileConflictStrategy.Fail -> SafConflictDecision(clean, shouldWrite = false, message = "Target already exists")
        }
    }

    private fun nextAvailableName(parent: DocumentFile, desiredName: String): String {
        val dot = desiredName.lastIndexOf('.')
        val base = if (dot > 0) desiredName.substring(0, dot) else desiredName
        val ext = if (dot > 0) desiredName.substring(dot) else ""
        var index = 1
        while (true) {
            val candidate = "$base ($index)$ext"
            if (parent.findFile(candidate) == null) return candidate
            index++
        }
    }
}

data class SafConflictDecision(
    val targetName: String,
    val shouldWrite: Boolean,
    val shouldReplace: Boolean = false,
    val existing: DocumentFile? = null,
    val message: String? = null,
)
