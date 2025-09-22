package com.louisgautier.sample.server.domain

import com.louisgautier.apicontracts.pojo.UserRegistrationResponseJson
import kotlinx.coroutines.flow.Flow

internal interface AuthenticationRepository {
    suspend fun registerAnonymously(): Flow<Result<UserRegistrationResponseJson>>
    suspend fun registerWith(
        email: String,
        password: String
    ): Flow<Result<UserRegistrationResponseJson>>

    suspend fun loginInWith(
        email: String,
        password: String
    ): Flow<Result<UserRegistrationResponseJson>>

    suspend fun refreshSession(refreshToken: String): Flow<Result<UserRegistrationResponseJson>>

    suspend fun resetPassword(email: String): Flow<Result<Unit>>
    suspend fun updateUserPassword(password: String): Flow<Result<Unit>>

    suspend fun logout(): Flow<Result<Unit>>
    suspend fun reconciliation(
        id: String,
        email: String,
        password: String
    ): Flow<Result<UserRegistrationResponseJson>>
}