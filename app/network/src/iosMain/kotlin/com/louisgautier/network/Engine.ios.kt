package com.louisgautier.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual val engineFactory: HttpClientEngineFactory<*> = Darwin