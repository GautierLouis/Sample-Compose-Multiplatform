package com.louisgautier.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.louisgautier.App
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(com.louisgautier.loadModules())
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Sample",
    ) {
        App()
    }
}