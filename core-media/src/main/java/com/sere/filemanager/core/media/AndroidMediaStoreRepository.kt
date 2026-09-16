package com.sere.filemanager.core.media

import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidMediaStoreRepository(private val context: Context) : MediaRepository {
    override suspend fun listImages(): List<MediaItem> = query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString(), "image")
    override suspend fun listAudio(): List<MediaItem> = query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString(), "audio")
    override suspend fun listVideo(): List<MediaItem> = query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString(), "video")

    private suspend fun query(collection: String, fallbackMimePrefix: String): List<MediaItem> = withContext(Dispatchers.IO) {
        val uri = android.net.Uri.parse(collection)
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
        )
        context.contentResolver.query(uri, projection, null, null, "${MediaStore.MediaColumns.DATE_MODIFIED} DESC")?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
            val mimeColumn = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
            buildList {
                while (cursor.moveToNext() && size < 300) {
                    val id = cursor.getLong(idColumn).toString()
                    val name = cursor.getString(nameColumn) ?: "media-$id"
                    val path = if (dataColumn >= 0) cursor.getString(dataColumn).orEmpty() else name
                    val mime = if (mimeColumn >= 0) cursor.getString(mimeColumn) else "$fallbackMimePrefix/*"
                    add(MediaItem(id = id, path = path, title = name, mimeType = mime))
                }
            }
        } ?: emptyList()
    }
}
