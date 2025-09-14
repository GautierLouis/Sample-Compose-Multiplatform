package com.louisgautier.sample

import com.louisgautier.network.networkModule
import com.louisgautier.shared.sharedModule
import com.louisgautier.shared.sharedPlatformModule

fun loadModules() = listOf(sharedPlatformModule, sharedModule, networkModule)