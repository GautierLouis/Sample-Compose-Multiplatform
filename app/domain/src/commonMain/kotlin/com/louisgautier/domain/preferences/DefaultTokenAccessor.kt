package com.louisgautier.domain.preferences

import com.louisgautier.domain.interfaces.AppPreferences
import com.louisgautier.network.interfaces.TokenAccessor

class DefaultTokenAccessor(
    private val appPreferences: AppPreferences
) : TokenAccessor {
    override suspend fun getUserToken(): String? {
        return appPreferences.getUserToken()
    }

    override suspend fun getUserRefreshToken(): String? {
        return appPreferences.getUserRefreshToken()
    }

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