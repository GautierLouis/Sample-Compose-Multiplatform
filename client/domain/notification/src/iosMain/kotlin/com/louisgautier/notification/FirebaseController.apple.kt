package com.louisgautier.notification

import firebase.FIRRemoteConfig
import firebase.FIRRemoteConfigSettings
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseController {

    private val rc: FIRRemoteConfig
        get() = FIRRemoteConfig.remoteConfig()

    init {
        val settings = FIRRemoteConfigSettings().apply {
            minimumFetchInterval = DEFAULT_MIN_FETCH_INTERVAL.toDouble()
        }
        rc.configSettings = settings
    }

    actual suspend fun fetchAndActivate(): Boolean = suspendCancellableCoroutine { cont ->
        rc.fetchWithCompletionHandler { status, error ->
            if (error != null) {
                cont.resume(false)
            } else {
                // activate
                rc.fetchAndActivateWithCompletionHandler { changed, actError ->
                    if (actError != null) {
                        cont.resume(false)
                    } else {
                        cont.resume(true)
                    }
                }
            }
        }
    }
}