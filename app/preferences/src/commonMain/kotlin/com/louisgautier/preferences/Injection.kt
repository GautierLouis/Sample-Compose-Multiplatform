package com.louisgautier.preferences

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.louisgautier.platform.ContextWrapper
import okio.Path.Companion.toPath
import org.koin.dsl.bind
import org.koin.dsl.module

val preferencesModule = module {
    single {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { getDatastoreFilePath(get<ContextWrapper>()).toPath() }
        )
    }
    single { DefaultAppPreferences(get()) } bind AppPreferences::class
}