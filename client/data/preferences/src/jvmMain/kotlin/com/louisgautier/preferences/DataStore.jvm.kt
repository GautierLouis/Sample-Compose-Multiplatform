package com.louisgautier.preferences

import com.louisgautier.utils.context.ContextWrapper
import java.io.File

actual fun getDatastoreFilePath(contextWrapper: ContextWrapper): String =
    File(System.getProperty("java.io.tmpdir"), dataStoreFileName).absolutePath
