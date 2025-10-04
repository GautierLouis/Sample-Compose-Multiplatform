package com.louisgautier.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.louisgautier.logger.AppLogger
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class FirebaseController() {

    private val firebaseApp by lazy {
        FirebaseApp.getInstance()
    }

    private val rc: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance(firebaseApp)
    }

    private val fm: FirebaseMessaging by lazy {
        FirebaseMessaging.getInstance()
    }

    init {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(DEFAULT_MIN_FETCH_INTERVAL)
            .build()

        rc.setConfigSettingsAsync(settings).addOnFailureListener {
            AppLogger.e("Unable to set config for RemoteConfig : ${it.localizedMessage ?: it.message ?: it.stackTraceToString()}")
        }

        fm.token
            .addOnSuccessListener { token ->
                AppLogger.d("FCM Token : $token")
            }
            .addOnFailureListener { exception ->
                AppLogger.e("Unable to get FCM token : ${exception.localizedMessage ?: exception.message ?: exception.stackTraceToString()}")
            }

    }

    actual suspend fun fetchAndActivate(): Boolean = suspendCancellableCoroutine { cont ->
        rc.fetchAndActivate()
            .addOnSuccessListener { activated ->
                AppLogger.i("RemoteConfig successfully activated")
                cont.resume(activated)
            }
            .addOnFailureListener { ex ->
                AppLogger.e("Unable to Fetch remote config : ${ex.localizedMessage ?: ex.message ?: ex.stackTraceToString()}")
                cont.resumeWithException(ex)
            }
    }

    actual suspend fun getToken(): String {
        return fm.token.await()
    }

    actual fun subscribeToTopic(topic: String) {
        fm.subscribeToTopic(topic)
    }

    actual fun unsubscribeFromTopic(topic: String) {
        fm.unsubscribeFromTopic(topic)
    }

}

