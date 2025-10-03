package com.louisgautier.firebase.remoteconfig

import com.louisgautier.firebase.FirebaseController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFeatureFlagsStore(
    private val rcReader: RemoteConfigReader,
    private val rcClient: FirebaseController, // will be used to refresh
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) : FeatureFlagsStore {

    private val _flags = MutableStateFlow(FeatureFlags()) // initial safe defaults
    override val flags: StateFlow<FeatureFlags> = _flags

    init {
        scope.launch {
            try {
                refresh()
            } catch (_: Throwable) {
                // ignore — we keep defaults
            }
        }
    }

    override suspend fun refresh(): Boolean {
        val activated = try {
            rcClient.fetchAndActivate()
        } catch (_: Throwable) {
            // If fetch fails, return false, keep old flags, already logged
            return false
        }

        // read values from rcReader and update state
        val updated = FeatureFlags(
            testValue = rcReader.getString(FeatureFlagsKeys.TEST_VALUE),
        )

        _flags.value = updated
        return activated
    }
}