package com.louisgautier.firebase.remoteconfig

import kotlinx.coroutines.flow.StateFlow

interface FeatureFlagsStore {
    /** A StateFlow emitting the current FeatureFlags. Always non-null. */
    val flags: StateFlow<FeatureFlags>

    /** Force refresh remote config (fetch + activate) and update flags. Returns true if activation succeeded. */
    suspend fun refresh(): Boolean
}