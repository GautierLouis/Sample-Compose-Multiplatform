package com.louisgautier.core

actual fun getDatastoreFilePath(contextWrapper: ContextWrapper): String {
    return contextWrapper.context.filesDir.resolve(dataStoreFileName).absolutePath
}