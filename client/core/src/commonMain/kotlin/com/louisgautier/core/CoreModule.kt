package com.louisgautier.core

import com.louisgautier.permission.permissionModule
import com.louisgautier.utils.utilsModule
import org.koin.dsl.module

val coreModule = module {
    includes(utilsModule, permissionModule)
}