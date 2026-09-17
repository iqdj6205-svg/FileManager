package com.sere.filemanager.core.remote

data class RemoteUploadPolicy(
    val maxBytes: Long = 25L * 1024L * 1024L,
    val allowedMimePrefixes: List<String> = emptyList(),
    val blockedExtensions: Set<String> = setOf("apk", "dex", "so"),
) {
    fun validate(fileName: String, sizeBytes: Long?): RemoteValidationResult {
        val extension = fileName.substringAfterLast('.', missingDelimiterValue = "").lowercase()
        if (extension in blockedExtensions) return RemoteValidationResult(false, "File type .$extension is blocked by default")
        if (sizeBytes != null && sizeBytes > maxBytes) return RemoteValidationResult(false, "File is larger than upload limit")
        return RemoteValidationResult(true)
    }
}
