package com.louisgautier.composeApp

import kotlinx.serialization.Serializable

@Serializable
sealed class Navigation {
    @Serializable
    data object Home : Navigation()
}