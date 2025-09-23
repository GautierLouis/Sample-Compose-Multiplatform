package com.louisgautier.composeApp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin

fun main() =
    application {
        startKoin {
            modules(loadModules())
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "Sample",
        ) {
            App()
        }
    }
