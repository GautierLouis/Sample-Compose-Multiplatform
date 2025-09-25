package com.louisgautier.domain

import com.louisgautier.domain.preferences.DefaultTokenAccessor
import com.louisgautier.domain.repository.AuthRepository
import com.louisgautier.network.interfaces.TokenAccessor
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule =
    module {
        single { AuthRepository(get(), get()) }
        single { DefaultTokenAccessor(get()) } bind TokenAccessor::class
    }
