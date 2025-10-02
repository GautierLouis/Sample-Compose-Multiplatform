package com.louisgautier.gallery

import org.koin.core.module.Module
import org.koin.dsl.module

val galleryModule = module {
    includes(galleryPlatformModule)
}

internal expect val galleryPlatformModule: Module