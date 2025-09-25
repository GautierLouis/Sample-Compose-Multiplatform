package com.louisgautier.utils

import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CompletableDeferred

/**
 * Observes activity results and provides a way to launch intents for results.
 *
 * This class uses a [CompletableDeferred] to suspend the coroutine until an activity result is received.
 * It requires an [ActivityResultLauncher] to be set via [setLauncher] before launching any intents using [launchIntent].
 * The [onActivityResult] method should be called by the component that receives the activity result (e.g., a Fragment or Activity)
 * to complete the suspended coroutine.
 */
abstract class DefaultActivityResultObserver<T>() {

    private var completer: CompletableDeferred<ActivityResult>? = null
    private lateinit var startForResult: ActivityResultLauncher<T>

    suspend fun launchIntent(intent: T): ActivityResult {
        completer = CompletableDeferred()
        startForResult.launch(intent)
        return completer!!.await()
    }

    fun setLauncher(launcher: ActivityResultLauncher<T>) {
        startForResult = launcher
    }

    fun onActivityResult(result: ActivityResult) {
        completer?.complete(result)
    }
}

