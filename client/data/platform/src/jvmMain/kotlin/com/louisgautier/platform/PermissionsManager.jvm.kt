package com.louisgautier.platform

actual class PermissionsManager actual constructor() {
    actual fun isPermissionGranted(permission: PermissionType): Boolean {
        return false
    }

    actual suspend fun requestPermission(
        permission: PermissionType,
        callback: PermissionCallback
    ) {
        callback.onPermissionStatus(permission, PermissionResult.DENIED)
    }
}