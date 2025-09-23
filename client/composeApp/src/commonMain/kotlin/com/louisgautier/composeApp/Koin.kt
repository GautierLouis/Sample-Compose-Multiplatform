package com.louisgautier.composeApp

import com.louisgautier.biometric.biometricModule
import com.louisgautier.domain.domainModule
import org.koin.core.module.Module

fun getAllModules(): List<Module> = listOf(domainModule) + featuresModule()

private fun featuresModule() = listOf(
    biometricModule
)
