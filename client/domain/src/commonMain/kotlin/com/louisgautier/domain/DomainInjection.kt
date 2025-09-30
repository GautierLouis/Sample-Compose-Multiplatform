package com.louisgautier.domain

import com.louisgautier.auth.authInjection
import com.louisgautier.data.dataModule
import org.koin.dsl.module

val domainModule = module {
    includes(authInjection, dataModule)
}