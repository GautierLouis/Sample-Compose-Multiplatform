package com.louisgautier.permission

import org.koin.core.module.Module
import org.koin.dsl.module

val permissionModule = module {
    single { PermissionHelper(get()) }
    includes(permissionPlatformModule)
}
internal expect val permissionPlatformModule: Module