package com.louisgautier.composeApp

import kotlinx.serialization.Serializable

@Serializable
sealed class MainNavigation {
    @Serializable
    data object Home : MainNavigation()

    @Serializable
    data object Login : MainNavigation()

    @Serializable
    data object Gallery : MainNavigation()

}