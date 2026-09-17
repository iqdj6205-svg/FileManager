package com.sere.filemanager.wear

import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat

class RemoteServiceController(private val context: Context) {
    fun start() {
        val intent = RemoteServerService.startIntent(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.startService(intent)
        }
    }

    fun stop() {
        context.startService(RemoteServerService.stopIntent(context))
    }
}
