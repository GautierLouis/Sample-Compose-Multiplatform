package com.louisgautier.notification

expect class FirebaseController {

    /**
     * Fetch remote values and immediately activate them.
     * Returns true if values were activated.
     */
    suspend fun fetchAndActivate(): Boolean

}

const val DEFAULT_MIN_FETCH_INTERVAL: Long = 3600L // 1 hour default
