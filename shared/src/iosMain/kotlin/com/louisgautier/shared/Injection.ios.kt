package com.louisgautier.shared

import org.koin.core.module.Module
import org.koin.dsl.module

actual val sharedPlatformModule: Module = module {
    single { createDataStore() }
}