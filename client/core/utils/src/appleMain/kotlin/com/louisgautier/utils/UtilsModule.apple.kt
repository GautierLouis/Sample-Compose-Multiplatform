package com.louisgautier.utils

import com.louisgautier.utils.context.ContextWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

actual val utilsModule: Module = module {
    single { ContextWrapper() }
}