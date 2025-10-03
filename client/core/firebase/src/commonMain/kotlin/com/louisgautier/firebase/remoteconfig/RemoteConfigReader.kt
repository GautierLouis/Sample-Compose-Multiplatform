package com.louisgautier.firebase.remoteconfig

/**
 * Small adapter to read keys from platform Remote Config.
 * Keep it tiny so actuals remain simple.
 */
interface RemoteConfigReader {
    fun getBoolean(key: String, default: Boolean = false): Boolean
    fun getString(key: String, default: String = ""): String
    fun getLong(key: String, default: Long = 0L): Long
}