package com.louisgautier.shared

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module

actual val sharedPlatformModule: Module = module {
    single<DataStore<Preferences>> { createDataStore(context = get()) }
}