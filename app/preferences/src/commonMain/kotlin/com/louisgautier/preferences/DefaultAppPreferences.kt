package com.louisgautier.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class DefaultAppPreferences(
    private val store: DataStore<Preferences>,
) : AppPreferences {

    companion object {
        private val USER_TOKEN = stringPreferencesKey("user_token")
        private val USER_REFRESH_TOKEN = stringPreferencesKey("user_refresh_token")
    }

    override suspend fun setUserToken(token: String) {
        store.edit { settings ->
            settings[USER_TOKEN] = token
        }
    }

    override suspend fun setUserRefreshToken(token: String) {
        store.edit { settings ->
            settings[USER_REFRESH_TOKEN] = token
        }
    }

    override fun getUserTokenAsFlow() = store.data.map { preferences -> preferences[USER_TOKEN] }
    override fun getUserRefreshTokenAsFlow() =
        store.data.map { preferences -> preferences[USER_REFRESH_TOKEN] }

    override suspend fun getUserToken() = getUserTokenAsFlow().firstOrNull()
    override suspend fun getUserRefreshToken() = getUserRefreshTokenAsFlow().firstOrNull()

    override suspend fun removeUserToken() {
        store.edit { settings ->
            settings.remove(USER_TOKEN)
            settings.remove(USER_REFRESH_TOKEN)
        }
    }
}