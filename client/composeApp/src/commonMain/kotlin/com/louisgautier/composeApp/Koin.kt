package com.louisgautier.composeApp

import com.louisgauteir.core.coreModule
import com.louisgautier.biometric.biometricModule
import com.louisgautier.domain.domainModule
import com.louisgautier.gallery.galleryModule
import com.louisgautier.login.loginModule
import org.koin.core.module.Module

fun getAllModules(): List<Module> = domainModule + featuresModule() + coreModule

private fun featuresModule() = listOf(
    biometricModule,
    galleryModule,
    loginModule
)