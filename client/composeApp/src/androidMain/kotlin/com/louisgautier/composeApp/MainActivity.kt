package com.louisgautier.composeApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.louisgautier.utils.IntentActivityResultObserver
import com.louisgautier.utils.PermissionActivityResultObserver
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level

class MainActivity : FragmentActivity() {

    private val intentActivityResultObserver: IntentActivityResultObserver by inject()
    private val permissionActivityResultObserver: PermissionActivityResultObserver by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            intentActivityResultObserver.onActivityResult(result)
        }

        val permissionLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            permissionActivityResultObserver.onActivityResult(results)
        }

        setContent {
            KoinApplication(application = {
                androidLogger(Level.DEBUG)
                androidContext(this@MainActivity)
                modules(getAllModules())
            }) {
                intentActivityResultObserver.setLauncher(activityResultLauncher)
                permissionActivityResultObserver.setLauncher(permissionLauncher)
                App()
            }
        }
    }
}
