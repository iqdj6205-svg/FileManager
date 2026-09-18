package com.sere.filemanager.wear

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.sere.filemanager.core.files.LocalFileRepository
import com.sere.filemanager.core.remote.RemoteConfig
import com.sere.filemanager.core.remote.RemoteServerController
import com.sere.filemanager.core.remote.RemoteServerControllerFactory
import com.sere.filemanager.core.remote.RemoteServerStatusStore

class RemoteServerService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val lifecyclePolicy = RemoteLifecyclePolicy()
    private var controller: RemoteServerController? = null
    private var activeConfig: RemoteConfig = RemoteConfig()
    private var startedAtMillis: Long? = null

    private val timeoutCheck = object : Runnable {
        override fun run() {
            WearStatusPublisher(this@RemoteServerService).publishRemoteStatus()
            if (lifecyclePolicy.shouldStopForTimeout(startedAtMillis, System.currentTimeMillis(), activeConfig)) {
                stopServerAndSelf()
            } else handler.postDelayed(this, 30_000L)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onCreate() { super.onCreate(); ensureChannel() }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) { ACTION_START -> startServer(); ACTION_STOP -> stopServerAndSelf() }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(timeoutCheck)
        controller?.let { runCatching { kotlinx.coroutines.runBlocking { it.stop() } } }
        RemoteServerStatusStore.clear()
        WearStatusPublisher(this).publishRemoteStatus()
        controller = null
        super.onDestroy()
    }

    private fun startServer() {
        val config = WearSettingsStore.current().remote.toRemoteConfig()
        activeConfig = config
        val battery = BatteryMonitor(this).batteryPercent()
        if (!lifecyclePolicy.shouldAllowStart(battery, config)) {
            RemoteServerStatusStore.clear()
            WearStatusPublisher(this).publishRemoteStatus()
            stopSelf()
            return
        }
        val server = RemoteServerControllerFactory.create(LocalFileRepository(), useEmbeddedPrototype = true)
        controller = server
        val session = kotlinx.coroutines.runBlocking { server.start() }
        RemoteServerStatusStore.update(session)
        WearStatusPublisher(this).publishRemoteStatus()
        startedAtMillis = session.startedAtMillis
        startForeground(NOTIFICATION_ID, notification("Remote: ${session.url} PIN ${session.pin}"))
        handler.postDelayed(timeoutCheck, 30_000L)
    }

    private fun stopServerAndSelf() {
        handler.removeCallbacks(timeoutCheck)
        controller?.let { runCatching { kotlinx.coroutines.runBlocking { it.stop() } } }
        RemoteServerStatusStore.clear()
        WearStatusPublisher(this).publishRemoteStatus()
        controller = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(NotificationChannel(CHANNEL_ID, "Remote access", NotificationManager.IMPORTANCE_LOW).apply { description = "Shown while FileManager remote access is running" })
        }
    }

    private fun notification(text: String): Notification {
        val stopIntent = PendingIntent.getService(this, 1, Intent(this, RemoteServerService::class.java).setAction(ACTION_STOP), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(this, CHANNEL_ID) else @Suppress("DEPRECATION") Notification.Builder(this)
        return builder.setContentTitle("FileManager").setContentText(text).setSmallIcon(android.R.drawable.stat_sys_upload).addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopIntent).setOngoing(true).build()
    }

    companion object {
        private const val CHANNEL_ID = "file_manager_remote"
        private const val NOTIFICATION_ID = 42
        const val ACTION_START = "com.sere.filemanager.wear.remote.START"
        const val ACTION_STOP = "com.sere.filemanager.wear.remote.STOP"
        fun startIntent(context: Context): Intent = Intent(context, RemoteServerService::class.java).setAction(ACTION_START)
        fun stopIntent(context: Context): Intent = Intent(context, RemoteServerService::class.java).setAction(ACTION_STOP)
    }
}

private fun com.sere.filemanager.core.model.RemoteServerSettings.toRemoteConfig(): RemoteConfig = RemoteConfig(
    port = port,
    requirePin = requirePin,
    allowUploads = allowUploads,
    allowDelete = allowDelete,
    autoStopMinutes = autoStopMinutes,
    localNetworkOnly = localNetworkOnly,
)
