package com.louisgautier.biometric

import com.louisgautier.utils.context.ContextWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

actual val biometricModule: Module =
    module {
        single { BiometricAuthenticator(get<ContextWrapper>(), get()) }
    }