package com.louisgautier.platform

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module

actual val contextModule: Module = module {
    single { ContextWrapper(get<Context>()) }
}