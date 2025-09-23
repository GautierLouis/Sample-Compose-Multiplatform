package com.louisgautier.database

import org.koin.dsl.module

val databaseModule =
    module {
        single { getRoomDatabase(get()) }
        single { get<AppDatabase>().getDummyDao() }
    }
