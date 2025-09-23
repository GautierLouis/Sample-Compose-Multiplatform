package com.louisgautier.network

import io.ktor.client.engine.HttpClientEngineFactory

internal expect val engineFactory: HttpClientEngineFactory<*>
