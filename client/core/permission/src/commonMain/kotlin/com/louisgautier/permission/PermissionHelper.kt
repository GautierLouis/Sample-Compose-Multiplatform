package com.louisgautier.permission

import kotlinx.coroutines.CompletableDeferred

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

    private val deferred: CompletableDeferred<PermissionResult> = CompletableDeferred()

    suspend fun checkOrAskForPermission(): PermissionResult {

        val result = permissionsManager.isPermissionGranted(PermissionType.GALLERY)
        when (result) {
            PermissionResult.GRANTED -> {
                deferred.complete(PermissionResult.GRANTED)
            }

            PermissionResult.DENIED -> ask()
        }

        return deferred.await()
    }

    private suspend fun ask() {
        permissionsManager.requestPermission(PermissionType.GALLERY, object : PermissionCallback {
            override fun onPermissionStatus(
                permissionType: PermissionType,
                status: PermissionResult
            ) {
                deferred.complete(status)
            }
        })
    }
}