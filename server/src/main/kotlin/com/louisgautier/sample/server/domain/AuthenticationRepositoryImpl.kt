package com.louisgautier.sample.server.domain

import com.louisgautier.apicontracts.dto.UserAuthMethodProvider
import com.louisgautier.apicontracts.dto.UserInfoJson
import com.louisgautier.apicontracts.dto.UserRegistrationResponseJson
import com.louisgautier.apicontracts.dto.UserTokenJson
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

internal class AuthenticationRepositoryImpl(
    private val client: SupabaseClient
) : AuthenticationRepository {

    private fun observeUserRegistration(
        state: Flow<SessionStatus>,
        provider: UserAuthMethodProvider
    ): Flow<Result<UserRegistrationResponseJson>> {
        return state.filter { it != SessionStatus.Initializing }
            .map {
                when (it) {
                    is SessionStatus.Authenticated -> Result.success(
                        buildUserResponse(
                            it.session,
                            provider
                        )
                    )

                    is SessionStatus.NotAuthenticated -> Result.failure(Throwable(""))
                    else -> Result.failure(Throwable(""))
                }
            }
    }

    private fun buildUserResponse(
        session: UserSession,
        provider: UserAuthMethodProvider
    ): UserRegistrationResponseJson {
        return UserRegistrationResponseJson(
            user = UserInfoJson(
                id = session.user!!.id,
                provider = listOfNotNull(provider),
            ),
            session = UserTokenJson(
                accessToken = session.accessToken,
                refreshToken = session.refreshToken,
                expiresIn = session.expiresIn,
            ),
        )
    }

    override suspend fun registerAnonymously(): Flow<Result<UserRegistrationResponseJson>> {
        client.auth.signInAnonymously()
        return observeUserRegistration(client.auth.sessionStatus, UserAuthMethodProvider.ANONYMOUS)
    }

    override suspend fun registerWith(
        email: String,
        password: String
    ): Flow<Result<UserRegistrationResponseJson>> {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        return observeUserRegistration(client.auth.sessionStatus, UserAuthMethodProvider.EMAIL)
    }

    override suspend fun reconciliation(
        id: String,
        email: String,
        password: String
    ): Flow<Result<UserRegistrationResponseJson>> {
        TODO("Not yet implemented")
    }

    override suspend fun loginInWith(
        email: String,
        password: String
    ): Flow<Result<UserRegistrationResponseJson>> {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        return observeUserRegistration(client.auth.sessionStatus, UserAuthMethodProvider.EMAIL)
    }

    override suspend fun refreshSession(refreshToken: String): Flow<Result<UserRegistrationResponseJson>> {
        client.auth.refreshSession(refreshToken = refreshToken)
        return observeUserRegistration(client.auth.sessionStatus, UserAuthMethodProvider.EMAIL)
    }

    override suspend fun resetPassword(email: String): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPassword(password: String): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

}