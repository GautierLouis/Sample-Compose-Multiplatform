package com.louisgautier.shared

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import okio.Path.Companion.toPath

internal fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )


class AppPreferences(private val store: DataStore<Preferences>) {

    companion object {
        private val USER_TOKEN = stringPreferencesKey("user_token")
        private val USER_REFRESH_TOKEN = stringPreferencesKey("user_refresh_token")
    }

    suspend fun setUserToken(token: String) {
        store.edit { settings ->
            settings[USER_TOKEN] = token
        }
    }

    suspend fun getUserToken(): String? {
        return store.data.map { preferences -> preferences[USER_TOKEN] }.single()
    }

    suspend fun setUserRefreshToken(token: String) {
        store.edit { settings ->
            settings[USER_REFRESH_TOKEN] = token
        }
    }

    suspend fun getUserRefreshToken(): String? {
        return store.data.map { preferences -> preferences[USER_REFRESH_TOKEN] }.single()
    }
}
