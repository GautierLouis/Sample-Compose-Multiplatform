package com.louisgautier.network

import com.louisgautier.shared.AppPreferences
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object NetworkQualifiers {
    val UnauthedHttpClient = named("UnauthedHttpClient")
    val AuthedHttpClient = named("AuthedHttpClient")
}


val networkModule: Module = module {
    single(NetworkQualifiers.UnauthedHttpClient) {
        HttpClient {
            apply(baseHttpClientConfig)
        }
    }

    single(NetworkQualifiers.AuthedHttpClient) {
        val preferences: AppPreferences = get()
        HttpClient {
            apply(baseHttpClientConfig)
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = preferences.getUserToken()
                        val refreshToken = preferences.getUserRefreshToken()
                        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                            null // No tokens available
                        } else {
                            BearerTokens(accessToken, refreshToken)
                        }
                    }
                }

            }
        }
    }

    single {
        AppService(
            get(NetworkQualifiers.UnauthedHttpClient),
            get(NetworkQualifiers.AuthedHttpClient)
        )
    }
}


//                    refreshTokens {
//                         val refreshService: RefreshTokenService = get() // Assuming you have such a service
//                         val newTokens = refreshService.refreshUserToken(appPreferences.getUserRefreshToken())
//                         if (newTokens != null) {
//                             appPreferences.saveUserToken(newTokens.accessToken)
//                             appPreferences.saveUserRefreshToken(newTokens.refreshToken)
//                             BearerTokens(newTokens.accessToken, newTokens.refreshToken)
//                         } else {
//                             // Failed to refresh, clear tokens or trigger logout
//                             appPreferences.clearUserTokens()
//                             null
//                         }
//                    }