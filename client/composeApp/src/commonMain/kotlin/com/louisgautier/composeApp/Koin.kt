package com.louisgautier.composeApp

import com.louisgauteir.core.coreModule
import com.louisgautier.biometric.biometricModule
import com.louisgautier.domain.domainModule
import org.koin.core.module.Module

fun getAllModules(): List<Module> = listOf(domainModule) + featuresModule() + coreModule()

private fun featuresModule() = listOf(
    biometricModule
)

private fun coreModule() = listOf(
    coreModule
)
