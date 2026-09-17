package com.sere.filemanager.wear

import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat

class RemoteServiceController(private val context: Context) {
    fun start(): Boolean {
        val intent = RemoteServerService.startIntent(context)
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, intent)
            } else {
                context.startService(intent)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun stop(): Boolean = try {
        context.startService(RemoteServerService.stopIntent(context))
        true
    } catch (e: Exception) {
        false
    }
}
