package com.louisgautier.platform

/**
 * Helper class for managing permissions.
 *
 * This class provides methods to check and request permissions.
 *
 * @property permissionsManager The [PermissionsManager] instance to use for permission operations.
 */
class PermissionHelper(
    private val permissionsManager: PermissionsManager
) {

    suspend fun checkOrAskForPermission(
        callback: (PermissionResult) -> Unit
    ) {

        val result = permissionsManager.isPermissionGranted(PermissionType.GALLERY)
        return when (result) {
            PermissionResult.GRANTED -> callback(PermissionResult.GRANTED)
            PermissionResult.DENIED -> ask {
                if (it == PermissionResult.GRANTED) {
                    callback(PermissionResult.GRANTED)
                } else callback(
                    PermissionResult.DENIED
                )
            }
        }
    }

    private suspend fun ask(callback: (PermissionResult) -> Unit) {
        permissionsManager.requestPermission(PermissionType.GALLERY, object : PermissionCallback {
            override fun onPermissionStatus(
                permissionType: PermissionType,
                status: PermissionResult
            ) {
                callback(status)
            }
        })
    }
}