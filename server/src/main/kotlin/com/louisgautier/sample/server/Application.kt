package com.louisgautier.sample.server

import com.louisgautier.apicontracts.pojo.NoteJson
import com.louisgautier.sample.server.database.configureDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.dataconversion.DataConversion
import kotlin.time.ExperimentalTime

fun main(args: Array<String>): Unit = EngineMain.main(args)

@OptIn(ExperimentalTime::class)
fun Application.module() {
    installInjection() // <- Inject first
    configureDatabase()
    installDataConversion()
    configureServer()
    configureRouting()
}

private fun Application.installDataConversion() {
    install(DataConversion) {
    }
}



