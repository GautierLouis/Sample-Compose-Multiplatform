package com.louisgautier.composeApp

import com.louisgautier.domain.domainModule
import org.koin.core.module.Module

fun loadModules(): List<Module> = listOf(domainModule)
