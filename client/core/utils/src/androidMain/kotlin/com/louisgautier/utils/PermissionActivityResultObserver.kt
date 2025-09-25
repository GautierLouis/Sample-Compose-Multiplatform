package com.louisgautier.utils

import android.app.Activity
import androidx.activity.result.ActivityResult

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