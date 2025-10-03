package com.louisgautier.composeApp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.louisgautier.composeApp.screen.Home
import com.louisgautier.gallery.GalleryNavigation
import com.louisgautier.gallery.galleryGraph
import com.louisgautier.login.LoginScreen

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = MainNavigation.Home,
        ) {
            composable<MainNavigation.Home> {
                Home { navigation ->
                    navController.navigate(navigation)
                }
            }

            composable<MainNavigation.Login> {
                LoginScreen { }
            }

            navigation<MainNavigation.Gallery>(GalleryNavigation.Home) {
                galleryGraph(navController)
            }
        }
    }
}
