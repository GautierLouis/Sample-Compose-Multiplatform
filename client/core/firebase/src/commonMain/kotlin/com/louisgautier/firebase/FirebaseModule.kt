package com.louisgautier.firebase

import com.louisgautier.firebase.remoteconfig.DefaultFeatureFlagsStore
import com.louisgautier.firebase.remoteconfig.FeatureFlagsStore
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

internal expect val firebasePlatformModule: Module

val firebaseModule = module {
    includes(firebasePlatformModule)
    single { DefaultFeatureFlagsStore(get(), get()) } bind FeatureFlagsStore::class
}