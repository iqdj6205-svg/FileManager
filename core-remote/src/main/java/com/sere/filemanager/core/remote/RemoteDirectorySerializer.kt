package com.sere.filemanager.core.remote

import com.sere.filemanager.core.model.FileItem

object RemoteDirectorySerializer {
    fun serialize(items: List<FileItem>): String = RemoteJson.array(items.map(::serializeItem))

    private fun serializeItem(item: FileItem): String = RemoteJson.obj(
        RemoteJson.property("name", item.name),
        RemoteJson.property("path", item.path),
        RemoteJson.property("type", item.type.name),
        RemoteJson.numberProperty("sizeBytes", item.sizeBytes ?: 0L),
    )
}
