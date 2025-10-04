package com.louisgautier.firebase

import com.louisgautier.firebase.notification.PushNotificationManager
import com.louisgautier.firebase.remoteconfig.AppleRemoteConfigReader
import com.louisgautier.firebase.remoteconfig.RemoteConfigReader
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalForeignApi::class)
actual val firebasePlatformModule = module {
    single { FirebaseController().also { it.init() } }
    single { AppleRemoteConfigReader() } bind RemoteConfigReader::class
    single { PushNotificationManager() }
}