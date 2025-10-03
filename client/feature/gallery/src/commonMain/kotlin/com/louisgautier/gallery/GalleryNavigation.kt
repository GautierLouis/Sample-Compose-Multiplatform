package com.louisgautier.gallery

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
sealed class GalleryNavigation {

    @Serializable
    data object Home : GalleryNavigation()
}


fun NavGraphBuilder.galleryGraph(navHost: NavHostController) {
    composable<GalleryNavigation.Home> {
        GalleryHome()
    }
}