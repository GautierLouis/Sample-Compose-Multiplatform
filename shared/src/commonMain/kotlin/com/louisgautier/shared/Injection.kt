package com.louisgautier.shared

import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule: Module = module {
    single { AppPreferences(get()) }
}

expect val sharedPlatformModule: Module