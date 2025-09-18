package com.louisgautier.sample.server

import com.louisgautier.apicontracts.pojo.NoteJson
import com.louisgautier.sample.server.database.entity.StoredNote
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.min
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class NoteRepository() {

    private var nextId = notes.maxOf { it.id }

    private val paginatedNotes =  notes.chunked(50)

    private fun StoredNote.serialize() = NoteJson(id, title, content, createdAt, updatedAt)
    private fun NoteJson.deserialize() = StoredNote(id, title, content, createdAt, updatedAt)

    fun getAllNotes(page: Int, limit: Int): List<NoteJson> {
        return paginatedNotes[min(page, paginatedNotes.size)]
            .take(limit)
            .map { it.serialize() }
    }

    fun getNoteById(id: Int): NoteJson? {
        return notes.firstOrNull { it.id == id }?.serialize()
    }

    fun createNote(json: StoredNote): StoredNote {
        val newNote = StoredNote(
            id = nextId,
            title = json.title,
            content = json.content,
            createdAt = json.createdAt,
            updatedAt = json.updatedAt
        )
        notes.add(newNote)
        return newNote
    }

    @OptIn(ExperimentalTime::class)
    fun updateNote(id: Int, title: String?, content: String?): StoredNote? {
        val note = notes.firstOrNull { it.id == id } ?: return null
        val updatedNote = note.copy(
            title = title ?: note.title,
            content = content ?: note.content,
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.Companion.currentSystemDefault())
        )
        notes.remove(note)
        notes.add(updatedNote)
        return updatedNote
    }
    fun deleteNote(id: Int): Boolean = notes.removeIf { it.id == id }
}