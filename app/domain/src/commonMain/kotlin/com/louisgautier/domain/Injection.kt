package com.louisgautier.domain

import com.louisgautier.core.coreModule
import com.louisgautier.database.databaseModule
import com.louisgautier.network.networkModule
import org.koin.dsl.module

val domainModule = module {
    includes(networkModule, coreModule, databaseModule)
    single { AuthRepository(get(), get()) }
}