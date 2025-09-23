package com.louisgautier.network.interfaces

/**
 * Interface for accessing and managing user authentication tokens.
 *
 * This interface acts as a crucial link between the :domain, :network and :preferences layers.
 *
 * The :network layer requires this interface to:
 * 1. Retrieve the currently stored user token (and refresh token) to authenticate API calls.
 * 2. Update the stored tokens (e.g., after a successful refresh) by persisting them,
 *    often to preferences or a secure storage mechanism.
 *
 * The :preferences layer typically owns the data storage mechanism (like DataStore or SharedPreferences)
 * and provides the concrete implementation of this interface.
 *
 * The :domain layer is the link between the :network and :preferences layers. This allows the
 * :network layer to remain agnostic about the specific storage details while ensuring tokens are
 * managed consistently.
 *
 * It provides methods to retrieve, set, and remove user tokens and refresh tokens.
 * Implementations are responsible for the secure and reliable storage and management of these tokens.
 */
interface TokenAccessor {
    suspend fun getUserToken(): String?
    suspend fun getUserRefreshToken(): String?
    suspend fun setUserToken(token: String)
    suspend fun setUserRefreshToken(refreshToken: String)
    suspend fun removeUserToken()
}