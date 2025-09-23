package com.louisgautier.network

import com.louisgautier.network.interfaces.AuthService
import com.louisgautier.network.interfaces.UserService
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module


val networkModule: Module = module {
    single { DefaultAuthService(get<DefaultService>().unauthedClient) } bind AuthService::class
    single { DefaultUserService(get<DefaultService>().authedClient) } bind UserService::class

    single { DefaultService(tokenAccessor = get()) }
}