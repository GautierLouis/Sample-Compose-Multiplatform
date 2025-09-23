package com.louisgautier.biometric

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

//TODO Move that somewhere else when needed
class AppScope() : CoroutineScope {
    val job = Job()
    val dispatcher = Dispatchers.Default
    val context = job + dispatcher

    override val coroutineContext: CoroutineContext = CoroutineScope(context).coroutineContext

}