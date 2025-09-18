package com.louisgautier.network

import org.koin.core.module.Module
import org.koin.dsl.module


val networkModule: Module = module {
    single { ApiClient(preferences = get()) }
}