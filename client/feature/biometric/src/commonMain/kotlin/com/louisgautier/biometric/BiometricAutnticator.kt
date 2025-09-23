package com.louisgautier.biometric

expect class BiometricAuthenticator {
    suspend fun authenticate(): Boolean
}