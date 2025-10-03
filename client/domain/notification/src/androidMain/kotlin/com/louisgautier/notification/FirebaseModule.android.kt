package com.louisgautier.notification

import org.koin.dsl.bind
import org.koin.dsl.module

actual val firebaseModule = module {
    single { FirebaseController() }
    single { AndroidRemoteConfigReader() } bind RemoteConfigReader::class
    single { AndroidFeatureFlagsStore(get(), get()) } bind FeatureFlagsStore::class

}