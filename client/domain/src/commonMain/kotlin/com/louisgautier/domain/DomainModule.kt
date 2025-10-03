package com.louisgautier.domain

import com.louisgautier.auth.authModule
import com.louisgautier.data.dataModule
import com.louisgautier.notification.firebaseModule
import org.koin.dsl.module

val domainModule = module {
    includes(authModule, dataModule, firebaseModule)
}