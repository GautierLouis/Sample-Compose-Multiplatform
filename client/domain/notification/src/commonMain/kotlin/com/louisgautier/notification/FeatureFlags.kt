package com.louisgautier.notification

/**
 * A typed representation of the flags your app cares about.
 * Add fields here that match the Remote Config keys you use in Firebase A/B tests.
 */
data class FeatureFlags(
    val testValue: String? = null,
) {
    companion object {
        fun fromMap(map: Map<String, Any?>): FeatureFlags {
            return FeatureFlags(
                testValue = map[FeatureFlag.TEST_VALUE] as? String,
            )
        }
    }
}