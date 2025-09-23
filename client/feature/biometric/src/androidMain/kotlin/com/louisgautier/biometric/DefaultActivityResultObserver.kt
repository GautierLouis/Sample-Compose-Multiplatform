package com.louisgautier.biometric

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CompletableDeferred

/**
 * Observes activity results and provides a way to launch intents for results.
 *
 * This class uses a [kotlinx.coroutines.CompletableDeferred] to suspend the coroutine until an activity result is received.
 * It requires an [androidx.activity.result.ActivityResultLauncher] to be set via [setLauncher] before launching any intents using [launchIntent].
 * The [onActivityResult] method should be called by the component that receives the activity result (e.g., a Fragment or Activity)
 * to complete the suspended coroutine.
 */
class DefaultActivityResultObserver() {

    private var completer: CompletableDeferred<ActivityResult>? = null

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    suspend fun launchIntent(intent: Intent): ActivityResult {
        completer = CompletableDeferred()
        startForResult.launch(intent)
        return completer!!.await()
    }

    fun setLauncher(launcher: ActivityResultLauncher<Intent>) {
        startForResult = launcher
    }

    fun onActivityResult(result: ActivityResult) {
        completer?.complete(result)
    }

}