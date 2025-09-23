package com.louisgautier.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.louisgautier.platform.ContextWrapper

actual fun getDatabaseBuilder(contextWrapper: ContextWrapper, databaseFileName: String): RoomDatabase.Builder<AppDatabase> {
    val appContext = contextWrapper.context.applicationContext
    val dbFile = appContext.getDatabasePath(databaseFileName)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}