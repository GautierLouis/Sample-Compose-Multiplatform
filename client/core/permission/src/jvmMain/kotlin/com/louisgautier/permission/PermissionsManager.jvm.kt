package com.louisgautier.permission

actual class PermissionsManager {
    actual fun isPermissionGranted(permission: PermissionType): PermissionResult {
        return PermissionResult.GRANTED
    }

    actual suspend fun requestPermission(
        permission: PermissionType,
        callback: PermissionCallback
    ) {
        callback.onPermissionStatus(permission, PermissionResult.GRANTED)
    }
}