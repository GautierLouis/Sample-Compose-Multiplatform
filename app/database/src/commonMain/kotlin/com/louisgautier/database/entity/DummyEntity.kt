package com.louisgautier.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dummy")
data class DummyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
)