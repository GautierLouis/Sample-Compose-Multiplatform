package com.louisgautier.sample.server.database

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


data class DatabaseConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String,
)

fun Application.configureDatabase() {

    val databaseConfig: DatabaseConfig by inject()

    with(databaseConfig) {
        if (user.isBlank()) {
            Database.connect(url = url, driver = driver)
        } else {
            Database.connect(url = url, driver = driver, user = user, password = password)
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