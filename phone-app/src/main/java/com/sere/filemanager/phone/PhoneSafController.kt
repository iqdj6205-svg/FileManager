package com.sere.filemanager.phone

import android.content.Context
import android.net.Uri
import com.sere.filemanager.core.files.SafFileOperations
import com.sere.filemanager.core.files.SafTreeAccess

class PhoneSafController(private val context: Context) {
    private val operations = SafFileOperations(context)

    fun persistAndName(uri: Uri): String {
        SafTreeAccess.persist(context, uri)
        return SafTreeAccess.displayName(context, uri)
    }

    fun list(uri: Uri) = operations.listTree(uri)
    fun createFolder(uri: Uri, name: String) = operations.createFolder(uri, name)
    fun rename(uri: Uri, name: String) = operations.rename(uri, name)
    fun delete(uri: Uri) = operations.delete(uri)
}
