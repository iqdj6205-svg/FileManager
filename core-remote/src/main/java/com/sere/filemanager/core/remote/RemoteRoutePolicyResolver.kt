package com.sere.filemanager.core.remote

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
