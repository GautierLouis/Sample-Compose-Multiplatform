package com.louisgautier.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.louisgautier.sample.App
import org.koin.core.context.startKoin

fun main() = application {
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