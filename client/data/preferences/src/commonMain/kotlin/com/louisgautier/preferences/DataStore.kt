package com.louisgautier.preferences

import com.louisgautier.utils.context.ContextWrapper

internal const val dataStoreFileName = "dice.preferences_pb"

internal expect fun getDatastoreFilePath(contextWrapper: ContextWrapper): String
