package com.louisgautier.sample.server.database.entity

import com.louisgautier.apicontracts.pojo.NoteJson
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object NoteTable : IntIdTable("note") {
    val title = varchar("title", 50)
    val content = varchar("content", 200)
    val createdAt = varchar("created_at", 50)
    val updatedAt = varchar("updated_at", 50)
}

class NoteDao(id: EntityID<Int>) : IntEntity(id) {
    companion object Companion : IntEntityClass<NoteDao>(NoteTable)

    var title by NoteTable.title
    var content by NoteTable.content
    var createdAt by NoteTable.createdAt
    var updatedAt by NoteTable.updatedAt

    override fun toString(): String {
        return "NoteDao(id=$id, title=$title, content=$content, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}