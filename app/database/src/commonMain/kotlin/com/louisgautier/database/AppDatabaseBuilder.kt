package com.louisgautier.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.louisgautier.platform.ContextWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal expect fun getDatabaseBuilder(
    contextWrapper: ContextWrapper,
    databaseFileName: String = "app_database.db",
): RoomDatabase.Builder<AppDatabase>

fun getRoomDatabase(contextWrapper: ContextWrapper): AppDatabase =
    getDatabaseBuilder(contextWrapper)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
