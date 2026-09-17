package com.sere.filemanager.wear

import com.sere.filemanager.core.model.RemoteSession
import com.sere.filemanager.core.remote.RemoteServerStatusStore

object RemoteStatusReader {
    fun current(): RemoteSession = RemoteServerStatusStore.current()
}
