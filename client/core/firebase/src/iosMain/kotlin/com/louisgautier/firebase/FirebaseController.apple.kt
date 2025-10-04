package com.louisgautier.firebase

import com.louisgautier.logger.AppLogger
import firebase.FIRMessaging
import firebase.FIRMessagingDelegateProtocol
import firebase.FIRRemoteConfig
import firebase.FIRRemoteConfigSettings
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.darwin.NSObject
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseController {

    private val rc: FIRRemoteConfig
        get() = FIRRemoteConfig.remoteConfig()

    private val fm: FIRMessaging
        get() = FIRMessaging.messaging()

    init {
        val settings = FIRRemoteConfigSettings().apply {
            minimumFetchInterval = DEFAULT_MIN_FETCH_INTERVAL.toDouble()
        }
        rc.configSettings = settings

        fm.delegate = object : NSObject(), FIRMessagingDelegateProtocol {
            override fun messaging(messaging: FIRMessaging, didReceiveRegistrationToken: String?) {
                AppLogger.d("FCM Token: $didReceiveRegistrationToken")
            }
        }
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

    actual suspend fun getToken(): String {
        return fm.FCMToken ?: ""
    }

    actual fun subscribeToTopic(topic: String) {
        fm.subscribeToTopic(topic, completion = null)
    }

    actual fun unsubscribeFromTopic(topic: String) {
        fm.unsubscribeFromTopic(topic, completion = null)
    }
}