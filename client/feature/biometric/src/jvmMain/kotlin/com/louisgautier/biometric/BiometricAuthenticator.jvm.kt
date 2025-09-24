package com.louisgautier.biometric

actual class BiometricAuthenticator {
    actual suspend fun authenticate(): Boolean {
        return true
    }
}