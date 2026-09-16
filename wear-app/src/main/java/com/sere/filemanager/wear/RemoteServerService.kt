package com.sere.filemanager.wear

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Foreground-service placeholder for long-running remote access.
 * The server is still controlled from ViewModel in the prototype; before beta,
 * remote serving should move here with a persistent notification.
 */
class RemoteServerService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
}
