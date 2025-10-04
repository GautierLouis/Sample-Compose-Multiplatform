package com.louisgautier.firebase

expect class FirebaseController {

    /**
     * RemoteConfig
     * Fetch remote values and immediately activate them.
     * Returns true if values were activated.
     */
    suspend fun fetchAndActivate(): Boolean

    /**
     * Messaging
     * Get the FCM token.
     */
    suspend fun getToken(): String

    /**
     * Messaging
     * Subscribe to a notification topic.
     */
    fun subscribeToTopic(topic: String)

    /**
     * Messaging
     * Unsubscribe from a notification topic.
     */
    fun unsubscribeFromTopic(topic: String)

}

const val DEFAULT_MIN_FETCH_INTERVAL: Long = 3600L // 1 hour default
