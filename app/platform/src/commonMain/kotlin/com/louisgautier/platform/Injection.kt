package com.louisgautier.platform

import org.koin.core.module.Module
import org.koin.dsl.module

val platformModule = module {
    includes(contextModule)
}

internal expect val contextModule: Module