package com.louisgautier.sample.server

import com.louisgautier.sample.server.database.configureDatabase
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import org.koin.core.module.Module

/**
 * Main entry point for the application
 * Refer to application.conf for configuration
 */
fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(
    envModule: Module = buildEnvModule(this.environment)
) {

    installModule(envModule)
    configureDatabase()
    configureServer()
    configureRouting()
}