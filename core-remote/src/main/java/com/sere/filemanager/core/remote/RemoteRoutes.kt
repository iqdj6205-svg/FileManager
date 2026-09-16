package com.sere.filemanager.core.remote

object RemoteRoutes {
    const val INDEX = "/"
    const val API_LIST = "/api/list"
    const val API_DOWNLOAD = "/api/download"
    const val API_UPLOAD = "/api/upload"
    const val API_RENAME = "/api/rename"
    const val API_DELETE = "/api/delete"
    const val API_MKDIR = "/api/mkdir"
    const val API_STATUS = "/api/status"
}

data class RemoteRoutePolicy(
    val route: String,
    val requiresPin: Boolean = true,
    val destructive: Boolean = false,
    val uploadsData: Boolean = false,
)

val DefaultRemoteRoutePolicies = listOf(
    RemoteRoutePolicy(RemoteRoutes.INDEX, requiresPin = false),
    RemoteRoutePolicy(RemoteRoutes.API_STATUS, requiresPin = true),
    RemoteRoutePolicy(RemoteRoutes.API_LIST, requiresPin = true),
    RemoteRoutePolicy(RemoteRoutes.API_DOWNLOAD, requiresPin = true),
    RemoteRoutePolicy(RemoteRoutes.API_UPLOAD, requiresPin = true, uploadsData = true),
    RemoteRoutePolicy(RemoteRoutes.API_RENAME, requiresPin = true, destructive = true),
    RemoteRoutePolicy(RemoteRoutes.API_DELETE, requiresPin = true, destructive = true),
    RemoteRoutePolicy(RemoteRoutes.API_MKDIR, requiresPin = true),
)
