package com.louisgautier.sample

import androidx.compose.ui.window.ComposeUIViewController
import com.louisgautier.sample.App
import org.koin.compose.KoinApplication

fun MainViewController() = ComposeUIViewController {
    KoinApplication(application = {
        modules(loadModules())
    }) {
        App()
    }
}