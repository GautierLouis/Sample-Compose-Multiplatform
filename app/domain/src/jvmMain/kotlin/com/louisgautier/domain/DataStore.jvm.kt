package com.louisgautier.domain

import com.louisgautier.platform.ContextWrapper
import java.io.File

actual fun getDatastoreFilePath(contextWrapper: ContextWrapper): String {
    return File(System.getProperty("java.io.tmpdir"), dataStoreFileName).absolutePath
}