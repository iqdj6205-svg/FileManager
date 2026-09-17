package com.sere.filemanager.core.files

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

object SafTreeAccess {
    fun persist(context: Context, uri: Uri) {
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        runCatching { context.contentResolver.takePersistableUriPermission(uri, flags) }
    }

    fun displayName(context: Context, uri: Uri): String {
        return DocumentFile.fromTreeUri(context, uri)?.name ?: "Selected folder"
    }
}
