package com.louisgautier.gallery

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val galleryModule = module {
    includes(galleryPlatformModule)
    viewModel { GalleryViewModel(get(), get()) }
}

internal expect val galleryPlatformModule: Module