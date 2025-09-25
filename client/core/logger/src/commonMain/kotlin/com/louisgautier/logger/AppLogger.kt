package com.louisgautier.logger

import co.touchlab.kermit.Logger

object AppLogger {
    fun w(
        message: String?,
        throwable: Throwable? = null,
        tag: String = Logger.Companion.tag,
    ) {
        Logger.Companion.w(messageString = message.orEmpty(), throwable = throwable, tag = tag)
    }

    fun e(
        message: String?,
        throwable: Throwable? = null,
        tag: String = Logger.Companion.tag,
    ) {
        Logger.Companion.e(messageString = message.orEmpty(), throwable = throwable, tag = tag)
    }

    fun i(
        message: String?,
        throwable: Throwable? = null,
        tag: String = Logger.Companion.tag,
    ) {
        Logger.Companion.i(messageString = message.orEmpty(), throwable = throwable, tag = tag)
    }

    fun d(
        message: String?,
        throwable: Throwable? = null,
        tag: String = Logger.Companion.tag,
    ) {
        Logger.Companion.d(messageString = message.orEmpty(), throwable = throwable, tag = tag)
    }
}