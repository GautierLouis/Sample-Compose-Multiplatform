package com.louisgautier.domain

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.louisgautier.database.databaseModule
import com.louisgautier.domain.interfaces.AppPreferences
import com.louisgautier.domain.preferences.DefaultAppPreferences
import com.louisgautier.domain.preferences.DefaultTokenAccessor
import com.louisgautier.domain.repository.AuthRepository
import com.louisgautier.network.interfaces.TokenAccessor
import com.louisgautier.network.networkModule
import com.louisgautier.platform.ContextWrapper
import com.louisgautier.platform.platformModule
import okio.Path.Companion.toPath
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    includes(networkModule, databaseModule, platformModule)

    single {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { getDatastoreFilePath(get<ContextWrapper>()).toPath() }
        )
    }
    single { DefaultAppPreferences(get()) } bind AppPreferences::class
    single { AuthRepository(get(), get()) }
    single { DefaultTokenAccessor(get()) } bind TokenAccessor::class
}