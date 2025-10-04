package com.louisgautier.composeApp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.louisgautier.designsystem.theme.button.Button
import com.louisgautier.designsystem.theme.button.ButtonType
import com.louisgautier.designsystem.theme.button.ButtonVariant
import com.louisgautier.firebase.notification.PushNotificationManager
import com.louisgautier.firebase.remoteconfig.FeatureFlagsStore
import com.louisgautier.gallery.LoadLocalPictures
import com.louisgautier.login.LoginScreen
import com.louisgautier.permission.PermissionHelper
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import sample.client.composeapp.generated.resources.Res
import sample.client.composeapp.generated.resources.greeting

@Composable
fun App(
    permissionManager: PermissionHelper = koinInject(),
    loadLocalPictures: LoadLocalPictures = koinInject(),
    flagStore: FeatureFlagsStore = koinInject(),
    notificationManager: PushNotificationManager = koinInject()
) {

    LaunchedEffect(Unit) {

    }


    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Navigation.Home,
        ) {
            composable<Navigation.Home> {
                Home { navigation ->
                    navController.navigate(navigation)
                }
            }

            composable<Navigation.Login> {
                LoginScreen { }
            }
        }
    }
}

@Composable
@Preview
fun Home(onNavigate: (Navigation) -> Unit = { }) {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    variant = ButtonVariant.GHOST,
                    type = ButtonType.SECONDARY,
                    enabled = true,
                    onClick = { onNavigate(Navigation.Login) },
                ) {
                    Text(stringResource(Res.string.greeting))
                }
            }
        }
    )
}
