package com.louisgautier.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class AppScope() : CoroutineScope {
    val job = Job()
    val dispatcher = Dispatchers.Default
    val context = job + dispatcher

    override val coroutineContext: CoroutineContext = CoroutineScope(context).coroutineContext

}