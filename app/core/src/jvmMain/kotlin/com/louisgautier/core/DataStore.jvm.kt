package com.louisgautier.core

import java.io.File

actual fun getDatastoreFilePath(contextWrapper: ContextWrapper): String {
    return File(System.getProperty("java.io.tmpdir"), dataStoreFileName).absolutePath
}