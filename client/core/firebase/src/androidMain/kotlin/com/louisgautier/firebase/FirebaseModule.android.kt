package com.louisgautier.firebase

import com.louisgautier.firebase.notification.PushNotificationManager
import com.louisgautier.firebase.remoteconfig.AndroidRemoteConfigReader
import com.louisgautier.firebase.remoteconfig.RemoteConfigReader
import org.koin.dsl.bind
import org.koin.dsl.module

actual val firebasePlatformModule = module {
    single { FirebaseController() }
    single { AndroidRemoteConfigReader() } bind RemoteConfigReader::class
    single { PushNotificationManager(get()) }
}