package com.louisgautier.core

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule: Module = module {
    includes(contextModule)
    single {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { getDatastoreFilePath(get<ContextWrapper>()).toPath() }
        )
    }
    single { AppPreferencesImpl(get()) } bind AppPreferences::class
}

internal expect val contextModule: Module
