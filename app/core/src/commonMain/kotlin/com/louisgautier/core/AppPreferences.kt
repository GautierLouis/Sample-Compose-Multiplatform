package com.louisgautier.core

import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    suspend fun setUserToken(token: String)
    suspend fun setUserRefreshToken(token: String)
    fun getUserTokenAsFlow(): Flow<String?>
    fun getUserRefreshTokenAsFlow(): Flow<String?>
    suspend fun getUserToken(): String?
    suspend fun getUserRefreshToken(): String?
    suspend fun removeUserToken()
}