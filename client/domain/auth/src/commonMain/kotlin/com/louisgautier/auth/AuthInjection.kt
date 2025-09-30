package com.louisgautier.auth

import com.louisgautier.network.interfaces.TokenAccessor
import org.koin.dsl.bind
import org.koin.dsl.module

val authInjection = module {
    single { DefaultTokenAccessor(get()) } bind TokenAccessor::class
    single { DefaultAuthRepository(get(), get()) } bind AuthRepository::class
}