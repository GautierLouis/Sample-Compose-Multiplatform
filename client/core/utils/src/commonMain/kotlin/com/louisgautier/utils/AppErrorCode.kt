package com.louisgautier.utils

sealed class AppErrorCode() : Throwable() {
    data object AppError : AppErrorCode()
    data object NetworkError : AppErrorCode()
    data object ServerError : AppErrorCode()
    data object InvalidCredentials : AppErrorCode()
    data object UnknownError : AppErrorCode()
}
