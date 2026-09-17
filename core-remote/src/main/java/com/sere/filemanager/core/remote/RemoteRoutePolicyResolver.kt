package com.sere.filemanager.core.remote

data class RemoteRoutePolicy(
    val route: String,
    val requiresPin: Boolean = true,
    val uploadsData: Boolean = false,
    val destructive: Boolean = false,
)

val DefaultRemoteRoutePolicies: List<RemoteRoutePolicy> = listOf(
    RemoteRoutePolicy(route = RemoteRoutes.API_STATUS),
    RemoteRoutePolicy(route = RemoteRoutes.API_AUDIT),
    RemoteRoutePolicy(route = RemoteRoutes.API_AUDIT_EXPORT),
    RemoteRoutePolicy(route = RemoteRoutes.API_LIST),
    RemoteRoutePolicy(route = RemoteRoutes.API_DOWNLOAD),
    RemoteRoutePolicy(route = RemoteRoutes.API_UPLOAD, uploadsData = true),
    RemoteRoutePolicy(route = RemoteRoutes.API_RENAME, destructive = true),
    RemoteRoutePolicy(route = RemoteRoutes.API_DELETE, destructive = true),
    RemoteRoutePolicy(route = RemoteRoutes.API_MKDIR, uploadsData = true),
)

class RemoteRoutePolicyResolver(
    private val policies: List<RemoteRoutePolicy> = DefaultRemoteRoutePolicies,
) {
    fun policyFor(path: String): RemoteRoutePolicy? = policies.firstOrNull { it.route == path }

    fun validateRoute(path: String, config: RemoteConfig, pin: String?): RemoteValidationResult {
        val policy = policyFor(path) ?: return RemoteValidationResult(false, "Unknown route")
        if (policy.requiresPin && config.requirePin && pin.isNullOrBlank()) return RemoteValidationResult(false, RemoteWebMessages.invalidPin)
        if (policy.uploadsData && !config.allowUploads) return RemoteValidationResult(false, RemoteWebMessages.uploadsDisabled)
        if (policy.destructive && !config.allowDelete) return RemoteValidationResult(false, RemoteWebMessages.destructiveDisabled)
        return RemoteValidationResult(true)
    }
}