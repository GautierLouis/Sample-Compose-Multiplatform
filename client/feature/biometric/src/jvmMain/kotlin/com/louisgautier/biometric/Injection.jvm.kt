package com.louisgautier.biometric

import org.koin.core.module.Module
import org.koin.dsl.module

actual val biometricModule: Module =
    module { single { BiometricAuthenticator() } }