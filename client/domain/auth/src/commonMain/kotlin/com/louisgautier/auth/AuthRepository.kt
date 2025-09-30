package com.louisgautier.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
}