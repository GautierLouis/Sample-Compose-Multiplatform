package com.louisgautier.platform

import android.app.Activity
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

class IntentActivityResultObserver() : DefaultActivityResultObserver<Intent>()
class PermissionActivityResultObserver() : DefaultActivityResultObserver<Array<String>>() {

    //[permission_code to granted_or_denied]
    fun onActivityResult(results: Map<String, Boolean>) {
        //if empty or one is denied, return cancel
        val code = if (results.isEmpty() || results.any { !it.value }) {
            Activity.RESULT_CANCELED
        } else Activity.RESULT_CANCELED

        onActivityResult(ActivityResult(resultCode = code, data = null))
    }
}