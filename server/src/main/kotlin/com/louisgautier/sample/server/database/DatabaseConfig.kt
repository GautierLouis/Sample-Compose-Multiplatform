package com.louisgautier.sample.server.database

import com.louisgautier.sample.server.DatabaseConfig
import com.louisgautier.sample.server.Environment
import com.louisgautier.sample.server.database.entity.NoteDao
import com.louisgautier.sample.server.database.entity.NoteTable
import com.louisgautier.sample.server.domain.notes
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject


fun Application.configureDatabase() {

    val environment: Environment by inject()
    val databaseConfig: DatabaseConfig by inject()

    if (environment != Environment.TEST) {
        with(databaseConfig) {
            if (user.isBlank()) {
                Database.connect(url = url, driver = "org.postgresql.Driver")
            } else {
                Database.connect(url = url, driver = "org.postgresql.Driver", user = user, password = password)
            }
        }

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(NoteTable)

            if (NoteDao.count() == 0L) {
                NoteTable.batchInsert(notes) {
                    this[NoteTable.title] = it.title
                    this[NoteTable.content] = it.content
                    this[NoteTable.createdAt] = it.createdAt.toString()
                    this[NoteTable.updatedAt] = it.updatedAt.toString()
                }
            }
        }
    }


}