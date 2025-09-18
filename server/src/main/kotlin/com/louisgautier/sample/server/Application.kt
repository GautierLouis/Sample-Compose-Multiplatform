package com.louisgautier.sample.server

import com.louisgautier.sample.server.database.configureDatabase
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import kotlin.time.ExperimentalTime

fun main(args: Array<String>): Unit = EngineMain.main(args)

@OptIn(ExperimentalTime::class)
fun Application.module() {
    installInjection() // <- Inject first
    configureDatabase()
    configureServer()
    configureRouting()
}




