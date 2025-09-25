package com.louisgautier.platform

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat

class AndroidPermissionsManager(
    private val contextWrapper: ContextWrapper,
    private val activityResultObserver: PermissionActivityResultObserver
) : PermissionsManager {

    override fun isPermissionGranted(permission: PermissionType): PermissionResult {
        return when {
            checkSelf(permission.toAndroidPermission()) -> {
                PermissionResult.GRANTED
            }

            shouldRationale(permission.toAndroidPermission()) -> {
                AppLogger.w("[PERMISSION] : A rationale must be asked for this permission ($permission)")
                PermissionResult.DENIED
            }

            else -> {
                PermissionResult.DENIED
            }
        }
    }

    private fun checkSelf(permissions: Array<String>): Boolean {
        return permissions.map {
            contextWrapper.context.checkSelfPermission(it)
        }.all {
            it == PackageManager.PERMISSION_GRANTED
        }
    }

    //TODO (implement Rationale?)
    private fun shouldRationale(permissions: Array<String>): Boolean {
        return permissions.map {
            ActivityCompat.shouldShowRequestPermissionRationale(
                contextWrapper.context as Activity,
                it
            )
        }.any { it }
    }

    override suspend fun requestPermission(
        permission: PermissionType,
        callback: PermissionCallback
    ) {
        val result = activityResultObserver.launchIntent(permission.toAndroidPermission())
        callback.onPermissionStatus(permission, result.toPermissionResult())
    }
}

private fun ActivityResult.toPermissionResult(): PermissionResult {
    return if (resultCode == Activity.RESULT_OK) PermissionResult.GRANTED else PermissionResult.DENIED
}

private fun PermissionType.toAndroidPermission(): Array<String> = when (this) {
    PermissionType.GALLERY -> galleryPermissions()
    else -> TODO()
}

private fun galleryPermissions(): Array<String> = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
        arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_VISUAL_USER_SELECTED)
    }

    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
        arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO)
    }

    else -> arrayOf(READ_EXTERNAL_STORAGE)
}