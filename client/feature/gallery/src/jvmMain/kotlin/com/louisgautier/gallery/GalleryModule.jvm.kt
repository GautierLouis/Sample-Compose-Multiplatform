package com.louisgautier.gallery

import org.koin.dsl.module

actual val galleryPlatformModule = module {
    single { LoadLocalPictures() }
}