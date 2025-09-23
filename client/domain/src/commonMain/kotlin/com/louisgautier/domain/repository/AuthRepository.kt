package com.louisgautier.domain.repository

import com.louisgautier.apicontracts.dto.UserJson
import com.louisgautier.network.interfaces.AuthService
import com.louisgautier.preferences.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class AuthRepository(
    private val client: AuthService,
    private val preferences: AppPreferences,
) {
    suspend fun login(): Result<Boolean> =
        client
            .login(UserJson(1, "test", "test"))
            .onSuccess { (token, refresh) ->
                preferences.setUserToken(token)
                preferences.setUserRefreshToken(refresh)
            }.map { true }

    fun getUserCreds(): Flow<Pair<String, String>> =
        preferences
            .getUserTokenAsFlow()
            .zip(preferences.getUserRefreshTokenAsFlow()) { token, refresh ->
                Pair(token.orEmpty(), refresh.orEmpty())
            }
}
