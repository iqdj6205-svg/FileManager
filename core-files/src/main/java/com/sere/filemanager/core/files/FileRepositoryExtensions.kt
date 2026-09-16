package com.sere.filemanager.core.files

import java.io.File

object FileRepositoryDefaults {
    fun likelyWearRoots(): List<File> = listOf(
        File("/sdcard"),
        File("/storage/emulated/0"),
        File(System.getProperty("user.home") ?: "/"),
    ).distinctBy { it.absolutePath }
}
