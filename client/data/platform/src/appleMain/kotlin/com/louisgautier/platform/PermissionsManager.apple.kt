package com.louisgautier.platform

import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary


class ApplePermissionsManager : PermissionsManager {
    override fun isPermissionGranted(permission: PermissionType): PermissionResult {
        return when (permission) {
            PermissionType.GALLERY -> {
                val status = PHPhotoLibrary.authorizationStatus() == PHAuthorizationStatusAuthorized
                if (status) PermissionResult.GRANTED else PermissionResult.DENIED
            }

            else -> {
                TODO("Not implemented yet!")
            }
        }
    }

    override suspend fun requestPermission(
        permission: PermissionType,
        callback: PermissionCallback
    ) {
        when (permission) {
            PermissionType.GALLERY -> {
                val status: PHAuthorizationStatus = PHPhotoLibrary.authorizationStatus()
                askGalleryPermission(status, permission, callback)
            }

            else -> {
                TODO("Not implemented yet!")
            }
        }
    }


    private fun askGalleryPermission(
        status: PHAuthorizationStatus,
        permission: PermissionType,
        callback: PermissionCallback
    ) {
        when (status) {
            PHAuthorizationStatusAuthorized -> {
                callback.onPermissionStatus(permission, PermissionResult.GRANTED)
            }

            PHAuthorizationStatusNotDetermined -> {
                PHPhotoLibrary.requestAuthorization { newStatus ->
                    askGalleryPermission(newStatus, permission, callback)
                }
            }

            PHAuthorizationStatusDenied -> {
                callback.onPermissionStatus(
                    permission, PermissionResult.DENIED
                )
            }

            else -> {
                AppLogger.e("unknown gallery status $status")
                PermissionResult.DENIED
            }
        }
    }
}