package com.louisgautier.biometric

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthentication
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics

actual class BiometricAuthenticator {

    private val context = LAContext()

    @OptIn(ExperimentalForeignApi::class)
    fun canAuthenticate(): Boolean {
        return context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthentication, error = null)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun authenticate(): Boolean =
        suspendCancellableCoroutine { continuation ->
            val can = canAuthenticate()

            context.evaluatePolicy(
                LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                localizedReason = "To protect your data, please authenticate using your biometric credentials"
            ) { success, error ->
                if (success) {
                    continuation.resumeWith(Result.success(true))
                } else {
                    continuation.resumeWith(Result.success(false))
                }
            }

            continuation.invokeOnCancellation {
                context.invalidate()
            }
        }
}