package com.louisgautier.network

import io.ktor.client.engine.HttpClientEngineFactory

expect val engineFactory: HttpClientEngineFactory<*>