package com.louisgautier.firebase.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class AndroidRemoteConfigReader(
    private val rc: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
) : RemoteConfigReader {

    override fun getBoolean(key: String, default: Boolean): Boolean =
        rc.getBoolean(key)

    override fun getString(key: String, default: String): String =
        rc.getString(key)

    override fun getLong(key: String, default: Long): Long =
        rc.getLong(key)

}