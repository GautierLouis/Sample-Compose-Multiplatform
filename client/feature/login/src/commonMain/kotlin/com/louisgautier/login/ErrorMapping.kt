package com.louisgautier.login

import com.louisgautier.utils.AppErrorCode
import org.jetbrains.compose.resources.StringResource
import sample.client.feature.login.generated.resources.Res
import sample.client.feature.login.generated.resources.error_invalid_credentials
import sample.client.feature.login.generated.resources.error_network
import sample.client.feature.login.generated.resources.error_server
import sample.client.feature.login.generated.resources.error_unknown


fun AppErrorCode.toResourceString(): StringResource = when (this) {
    AppErrorCode.AppError -> Res.string.error_unknown // or your real key
    AppErrorCode.NetworkError -> Res.string.error_network
    AppErrorCode.ServerError -> Res.string.error_server
    AppErrorCode.InvalidCredentials -> Res.string.error_invalid_credentials
    else -> Res.string.error_unknown
}
