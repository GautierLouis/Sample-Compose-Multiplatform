package com.louisgautier.biometric

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.louisgautier.platform.ContextWrapper
import com.louisgautier.platform.IntentActivityResultObserver
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

/**
 * Android implementation of [com.louis.gautier.biometric.BiometricAuthenticator].
 *
 * This class handles biometric authentication on Android devices. It uses the AndroidX Biometric library
 * to display a biometric prompt and interact with the system's biometric hardware.
 *
 * If the user has not enrolled any biometrics, this class will attempt to guide them through the
 * enrollment process by launching the system's biometric enrollment settings. The `activityResultObserver`
 * is crucial for this, as it allows the class to receive the result of this enrollment flow.
 *
 * The authentication process is asynchronous and uses a [CompletableDeferred] to signal its completion.
 * The `authenticate()` method will suspend until the biometric prompt is dismissed (either successfully,
 * with an error, or by cancellation) or the enrollment flow concludes.
 *
 * @param contextWrapper The application context injected by Koin.
 * @param activityResultObserver An observer for activity results, used to handle the biometric enrollment flow.
 *                               This is necessary to determine if the user successfully enrolled biometrics
 *                               after being redirected to the system settings.
 */
actual class BiometricAuthenticator(
    private val contextWrapper: ContextWrapper,
    private val activityResultObserver: IntentActivityResultObserver
) {

    private val biometricManager = BiometricManager.from(contextWrapper.context)
    private val executor = ContextCompat.getMainExecutor(contextWrapper.context)
    private val scope = AppScope()

    private var authenticationCompleter: CompletableDeferred<Boolean>? = null

    actual suspend fun authenticate(): Boolean {

        authenticationCompleter = CompletableDeferred()

        val authStatus = biometricManager.canAuthenticate(Authenticators.BIOMETRIC_STRONG)

        //Prompt user to set up biometric authentication
        if (authStatus == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enrollUser()
        }

        //No biometric authentication available
        if (authStatus != BIOMETRIC_SUCCESS) {
            authenticationCompleter?.complete(true)
        }

        val biometricPrompt = BiometricPrompt(
            contextWrapper.context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    scope.launch {
                        authenticationCompleter?.complete(true)
                    }
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication Required")
            .setSubtitle("To protect your data, please authenticate using your biometric credentials")
            .setConfirmationRequired(false)
            .setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)

        return authenticationCompleter!!.await()
    }

    @SuppressLint("InlinedApi")
    private suspend fun enrollUser() {
        val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            )
        }
        val enrollmentResult = activityResultObserver.launchIntent(intent)
        if (enrollmentResult.resultCode == Activity.RESULT_OK) {
            authenticate()
        }
    }
}