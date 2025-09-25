package com.louisgautier.permission


/**
 * Manages platform-specific permissions.
 *
 * This interface provides a way to check if a permission is granted and to request a permission.
 */
expect class PermissionsManager {
    fun isPermissionGranted(permission: PermissionType): PermissionResult
    suspend fun requestPermission(permission: PermissionType, callback: PermissionCallback)
}

interface PermissionCallback {
    fun onPermissionStatus(permissionType: PermissionType, status: PermissionResult)
}

enum class PermissionResult {
    GRANTED,
    DENIED,
}

enum class PermissionType {
    GALLERY,
    LOCALISATION,
    NOTIFICATION
}