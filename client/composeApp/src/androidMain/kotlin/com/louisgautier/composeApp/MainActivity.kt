package com.louisgautier.composeApp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.louisgautier.biometric.DefaultActivityResultObserver
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level

class MainActivity : FragmentActivity() {

    private val activityResultObserver: DefaultActivityResultObserver by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            activityResultObserver.onActivityResult(result)
        }

        setContent {
            KoinApplication(application = {
                androidLogger(Level.DEBUG)
                androidContext(this@MainActivity)
                modules(getAllModules())
            }) {
                activityResultObserver.setLauncher(launcher)
                App()
            }
        }
    }
}
