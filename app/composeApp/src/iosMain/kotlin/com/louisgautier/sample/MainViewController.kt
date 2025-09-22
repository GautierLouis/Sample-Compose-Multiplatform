package com.louisgautier.sample

import androidx.compose.ui.window.ComposeUIViewController
import com.louisgautier.App
import org.koin.compose.KoinApplication

fun MainViewController() = ComposeUIViewController {
    KoinApplication(application = {
        modules(com.louisgautier.loadModules())
    }) {
        App()
    }
}