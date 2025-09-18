package com.louisgautier.sample.server.domain

import com.louisgautier.apicontracts.pojo.NoteJson
import com.louisgautier.sample.server.database.entity.NoteDao
import com.louisgautier.sample.server.database.entity.NoteTable
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

internal class NoteRepository() {

    fun ResultRow.toNote() = NoteJson(
        id = this[NoteTable.id].value,
        title = this[NoteTable.title],
        content = this[NoteTable.content],
        createdAt = LocalDateTime.parse(this[NoteTable.createdAt]),
        updatedAt = LocalDateTime.parse(this[NoteTable.updatedAt])
    )

    suspend fun getAllNotes(page: Int, limit: Int): List<NoteJson> = suspendTransaction {
        NoteTable.selectAll()
            .limit(limit)
            .offset(page * limit.toLong())
            .map { n -> n.toNote() }
    }

    suspend fun getNoteById(id: Int): NoteJson? = suspendTransaction {
        NoteTable.selectAll().where { NoteTable.id eq id }.firstOrNull()?.toNote()
    }

    suspend fun createNote(json: NoteJson): Boolean = suspendTransaction {
        NoteTable.insert {
            it[title] = json.title
            it[content] = json.content
            it[createdAt] = json.createdAt.toString()
            it[updatedAt] = json.updatedAt.toString()
        }.isIgnore
    }
}