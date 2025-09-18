package com.louisgautier.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.java.Java

actual val engineFactory: HttpClientEngineFactory<*> = Java