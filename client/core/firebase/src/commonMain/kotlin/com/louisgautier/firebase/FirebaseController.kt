package com.louisgautier.firebase

import com.louisgautier.utils.context.ContextWrapper

expect class FirebaseController {

    fun init(contextWrapper: ContextWrapper? = null)
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
