package com.sere.filemanager.phone

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.sere.filemanager.core.wearbridge.WearFileTransferRequest
import com.sere.filemanager.core.wearbridge.WearTransferDirection

class PhoneTransferController(
    private val context: Context,
    private val channelClient: WearChannelTransferClient = WearChannelTransferClient(context),
) {
    fun describe(uri: Uri): PickedFile {
        var name = "selected-file.bin"
        var size: Long? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                if (nameIndex >= 0) name = cursor.getString(nameIndex) ?: name
                if (sizeIndex >= 0 && !cursor.isNull(sizeIndex)) size = cursor.getLong(sizeIndex)
            }
        }
        return PickedFile(uri, name, size)
    }

    suspend fun sendToWatch(file: PickedFile, targetDirectory: String) = channelClient.sendFileToFirstWatch(
        WearFileTransferRequest(
            fileName = file.displayName,
            targetPath = targetDirectory.trimEnd('/') + "/" + file.displayName,
            direction = WearTransferDirection.PhoneToWatch,
            sizeBytes = file.sizeBytes,
        ),
        file.uri,
    )
}
