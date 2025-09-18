package com.louisgautier.sample.server.database.entity

import kotlinx.datetime.LocalDateTime

data class StoredNote(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)