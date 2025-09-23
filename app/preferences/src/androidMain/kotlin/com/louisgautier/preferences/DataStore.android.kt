package com.louisgautier.preferences

import com.louisgautier.platform.ContextWrapper

actual fun getDatastoreFilePath(contextWrapper: ContextWrapper): String {
    return contextWrapper.context.filesDir.resolve(dataStoreFileName).absolutePath
}