package com.louisgautier.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.louisgautier.platform.ContextWrapper
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun getDatabaseBuilder(
    contextWrapper: ContextWrapper,
    databaseFileName: String,
): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + '/' + databaseFileName
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    return requireNotNull(documentDirectory?.path)
}
