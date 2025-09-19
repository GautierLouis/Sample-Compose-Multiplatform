package com.louisgautier.sample.server.database

import com.louisgautier.sample.server.DatabaseConfig
import com.louisgautier.sample.server.Environment
import com.louisgautier.sample.server.database.entity.NoteDao
import com.louisgautier.sample.server.database.entity.NoteTable
import com.louisgautier.sample.server.domain.notes
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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

        val config = HikariConfig().apply {
            jdbcUrl = databaseConfig.url
            username = databaseConfig.user
            password = databaseConfig.password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            addDataSourceProperty("sslmode", "require")
        }

        val ds = HikariDataSource(config)

        Database.connect(ds)

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