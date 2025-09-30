package com.louisgautier.auth

import com.louisgautier.apicontracts.dto.AuthUserJson
import com.louisgautier.network.interfaces.AuthService
import com.louisgautier.preferences.AppPreferences

class DefaultAuthRepository(
    private val client: AuthService,
    private val preferences: AppPreferences,
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> =
        client
            .login(AuthUserJson(email, password))
            .onSuccess { (token, refresh) ->
                preferences.setUserToken(token)
                preferences.setUserRefreshToken(refresh)
            }.map {}
}

