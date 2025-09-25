package com.louisgautier.data

import com.louisgautier.database.databaseModule
import com.louisgautier.network.networkModule
import com.louisgautier.preferences.preferencesModule
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule, databaseModule, preferencesModule)
}