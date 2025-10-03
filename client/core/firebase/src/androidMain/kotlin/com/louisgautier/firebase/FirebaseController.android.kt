package com.louisgautier.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.louisgautier.logger.AppLogger
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class FirebaseController : KoinComponent {

    private val firebaseApp by lazy {
        FirebaseApp.getInstance()
    }

    private val rc: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance(firebaseApp)
    }

    init {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(DEFAULT_MIN_FETCH_INTERVAL)
            .build()

        rc.setConfigSettingsAsync(settings).addOnFailureListener {
            AppLogger.e("Unable to set config for RemoteConfig : ${it.localizedMessage ?: it.message ?: it.stackTraceToString()}")
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
}

