package com.louisgautier.notification

import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalForeignApi::class)
actual val firebasePlatformModule = module {
    single { FirebaseController() }
    single { AppleRemoteConfigReader() } bind RemoteConfigReader::class
}