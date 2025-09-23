package com.louisgautier.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.louisgautier.platform.ContextWrapper
import java.io.File

actual fun getDatabaseBuilder(
    contextWrapper: ContextWrapper,
    databaseFileName: String,
): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), databaseFileName)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}
