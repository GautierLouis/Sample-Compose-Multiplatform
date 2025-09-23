package com.louisgautier.platform

import org.koin.core.module.Module
import org.koin.dsl.module

actual val contextModule: Module =
    module {
        single { ContextWrapper() }
    }
