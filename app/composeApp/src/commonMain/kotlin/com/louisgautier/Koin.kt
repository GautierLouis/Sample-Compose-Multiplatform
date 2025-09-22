package com.louisgautier


import com.louisgautier.domain.domainModule
import org.koin.core.module.Module


fun loadModules(): List<Module> {
    return listOf(domainModule)
}