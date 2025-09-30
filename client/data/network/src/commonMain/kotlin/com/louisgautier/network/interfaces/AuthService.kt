package com.louisgautier.network.interfaces

import com.louisgautier.apicontracts.dto.AuthUserJson
import com.louisgautier.apicontracts.dto.UserJson
import com.louisgautier.apicontracts.dto.UserRefreshTokenJson
import com.louisgautier.apicontracts.dto.UserTokenJson

interface AuthService {
    suspend fun registerAnon(): Result<UserTokenJson>
    suspend fun register(user: UserJson): Result<UserTokenJson>
    suspend fun login(user: AuthUserJson): Result<UserTokenJson>
    suspend fun forceRefresh(token: UserRefreshTokenJson): Result<UserTokenJson>
}
