package com.louisgautier.permission

import org.koin.core.module.Module
import org.koin.dsl.module

actual val permissionPlatformModule: Module = module {
    single { PermissionsManager() }
}