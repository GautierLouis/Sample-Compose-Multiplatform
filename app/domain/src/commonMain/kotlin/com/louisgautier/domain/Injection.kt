package com.louisgautier.domain

import com.louisgautier.database.databaseModule
import com.louisgautier.domain.preferences.DefaultTokenAccessor
import com.louisgautier.domain.repository.AuthRepository
import com.louisgautier.network.interfaces.TokenAccessor
import com.louisgautier.network.networkModule
import com.louisgautier.preferences.preferencesModule
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule =
    module {
        includes(networkModule, databaseModule, preferencesModule)

        single { AuthRepository(get(), get()) }
        single { DefaultTokenAccessor(get()) } bind TokenAccessor::class
    }
