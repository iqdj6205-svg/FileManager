package com.sere.filemanager.core.files

object OperationNamePolicy {
    fun duplicateName(original: String): String {
        val dot = original.lastIndexOf('.')
        return if (dot > 0) original.substring(0, dot) + " copy" + original.substring(dot) else "$original copy"
    }

    fun sanitizeInputName(value: String): String = value.trim().replace('/', '_').replace('\\', '_')

    fun isValidFileName(value: String): Boolean {
        val clean = sanitizeInputName(value)
        return clean.isNotBlank() && clean != "." && clean != ".." && clean.length <= 160
    }
}
