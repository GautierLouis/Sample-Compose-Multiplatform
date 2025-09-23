package com.louisgautier.domain.preferences

import com.louisgautier.network.interfaces.TokenAccessor
import com.louisgautier.preferences.AppPreferences

class DefaultTokenAccessor(
    private val appPreferences: AppPreferences,
) : TokenAccessor {
    override suspend fun getUserToken(): String? = appPreferences.getUserToken()

    override suspend fun getUserRefreshToken(): String? = appPreferences.getUserRefreshToken()

    override suspend fun setUserToken(token: String) {
        appPreferences.setUserToken(token)
    }

    override suspend fun setUserRefreshToken(refreshToken: String) {
        appPreferences.setUserRefreshToken(refreshToken)
    }

    override suspend fun removeUserToken() {
        appPreferences.removeUserToken()
    }
}
