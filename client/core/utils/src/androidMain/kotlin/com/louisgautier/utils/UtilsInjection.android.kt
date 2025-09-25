package com.louisgautier.utils

import android.content.Context
import com.louisgautier.utils.context.ContextWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

actual val utilsModule: Module = module {
    single { ContextWrapper(get<Context>()) }
    single { IntentActivityResultObserver() }
    single { PermissionActivityResultObserver() }
}