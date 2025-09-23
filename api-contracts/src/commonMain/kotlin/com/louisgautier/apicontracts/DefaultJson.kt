package com.louisgautier.apicontracts

import kotlinx.serialization.json.Json

val defaultJson = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    allowStructuredMapKeys = true
}