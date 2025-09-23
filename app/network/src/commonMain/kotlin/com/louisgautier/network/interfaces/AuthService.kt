package com.louisgautier.network.interfaces

import com.louisgautier.apicontracts.pojo.UserJson
import com.louisgautier.apicontracts.pojo.UserRefreshTokenJson
import com.louisgautier.apicontracts.pojo.UserTokenJson

interface AuthService {
    suspend fun registerAnon(): Result<UserTokenJson>
    suspend fun register(user: UserJson): Result<UserTokenJson>
    suspend fun login(user: UserJson): Result<UserTokenJson>
    suspend fun forceRefresh(token: UserRefreshTokenJson): Result<UserTokenJson>
}