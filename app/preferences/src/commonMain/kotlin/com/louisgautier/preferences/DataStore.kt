package com.louisgautier.preferences

import com.louisgautier.platform.ContextWrapper

internal const val dataStoreFileName = "dice.preferences_pb"

internal expect fun getDatastoreFilePath(contextWrapper: ContextWrapper): String