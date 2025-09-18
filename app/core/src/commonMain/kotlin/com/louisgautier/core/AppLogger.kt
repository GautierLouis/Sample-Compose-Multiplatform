package com.louisgautier.core

import co.touchlab.kermit.Logger


object AppLogger {

    fun w(
        message: String?,
        throwable: Throwable? = null,
        tag: String = Logger.tag,
    ) {
        Logger.w(messageString = message.orEmpty(), throwable = throwable, tag = tag)
    }

    fun e(
        message: String?,
        throwable: Throwable? = null,
        tag: String = Logger.tag,
    ) {
        Logger.e(messageString = message.orEmpty(), throwable = throwable, tag = tag)
    }

    fun i(
        message: String?,
        throwable: Throwable? = null,
        tag: String = Logger.tag,
    ) {
        Logger.i(messageString = message.orEmpty(), throwable = throwable, tag = tag)
    }

    fun d(
        message: String?,
        throwable: Throwable? = null,
        tag: String = Logger.tag,
    ) {
        Logger.d(messageString = message.orEmpty(), throwable = throwable, tag = tag)
    }

}