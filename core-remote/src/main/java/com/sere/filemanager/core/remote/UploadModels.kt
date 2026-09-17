package com.sere.filemanager.core.remote

data class UploadLimits(
    val maxBytes: Long = 25L * 1024L * 1024L,
    val allowedWhenBatteryBelowPercent: Int = 30,
)

data class UploadPlan(
    val targetDirectory: String,
    val fileName: String,
    val maxBytes: Long,
)

class UploadPlanner(
    private val limits: UploadLimits = UploadLimits(),
) {
    fun plan(targetDirectory: String, fileName: String): Result<UploadPlan> = runCatching {
        val validation = RemotePathGuard.validate(targetDirectory)
        require(validation == null) { validation ?: "Invalid target directory" }
        require(fileName.isNotBlank()) { "File name is empty" }
        require(!fileName.contains('/') && !fileName.contains('\\')) { "Nested upload names are not allowed" }
        UploadPlan(targetDirectory = targetDirectory, fileName = fileName, maxBytes = limits.maxBytes)
    }
}
