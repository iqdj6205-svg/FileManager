package com.sere.filemanager.core.media

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidMediaStoreRepository(private val context: Context) : MediaRepository {
    override suspend fun listImages(): List<MediaItem> = query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image")
    override suspend fun listAudio(): List<MediaItem> = query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "audio")
    override suspend fun listVideo(): List<MediaItem> = query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video")

    private suspend fun query(collection: Uri, fallbackMimePrefix: String): List<MediaItem> = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DURATION,
        )
        context.contentResolver.query(collection, projection, null, null, "${MediaStore.MediaColumns.DATE_MODIFIED} DESC")?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
            val mimeColumn = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
            val sizeColumn = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)
            val bucketColumn = cursor.getColumnIndex(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndex(MediaStore.MediaColumns.DURATION)
            buildList {
                while (cursor.moveToNext() && size < 300) {
                    val id = cursor.getLong(idColumn).toString()
                    val name = cursor.getString(nameColumn) ?: "media-$id"
                    val path = if (dataColumn >= 0) cursor.getString(dataColumn).orEmpty() else name
                    val mime = if (mimeColumn >= 0) cursor.getString(mimeColumn) else "$fallbackMimePrefix/*"
                    val size = if (sizeColumn >= 0) cursor.getLong(sizeColumn) else -1L
                    val bucket = if (bucketColumn >= 0) cursor.getString(bucketColumn) else null
                    val duration = if (durationColumn >= 0) cursor.getLong(durationColumn) else 0L
                    add(
                        MediaItem(
                            id = id,
                            path = path,
                            title = name,
                            mimeType = mime,
                            durationMillis = if (duration > 0) duration else null,
                            displayName = name,
                            uri = ContentUris.withAppendedId(collection, id.toLong()).toString(),
                            bucketName = bucket,
                            sizeBytes = if (size >= 0) size else null,
                        )
                    )
                }
            }
        } ?: emptyList()
    }
}