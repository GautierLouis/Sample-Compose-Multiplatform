package com.louisgautier.firebase.remoteconfig

import firebase.FIRRemoteConfig
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class AppleRemoteConfigReader(
    private val rc: FIRRemoteConfig = FIRRemoteConfig.Companion.remoteConfig()
) : RemoteConfigReader {

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return rc.configValueForKey(key).boolValue
    }

    override fun getString(key: String, default: String): String {
        return rc.configValueForKey(key).stringValue
    }

    override fun getLong(key: String, default: Long): Long {
        return rc.configValueForKey(key).numberValue.longLongValue
    }
}