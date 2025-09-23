package com.louisgautier.composeApp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.louisgautier.biometric.BiometricAuthenticator
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Serializable
sealed class Navigation {
    @Serializable
    data object Home : Navigation()

    @Serializable
    data object LoginByPhone : Navigation()

    @Serializable
    data class SendOTP(
        val mail: String,
    ) : Navigation()

    @Serializable
    data object FailAuth : Navigation()

    @Serializable
    data object Notes : Navigation()

    @Serializable
    data class Note(
        val id: Long,
    ) : Navigation()
}

@Composable
fun App(
    biometricAuthenticator: BiometricAuthenticator = koinInject()
) {

    LaunchedEffect(Unit) {
        biometricAuthenticator.authenticate()
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

//            composable<Navigation.LoginByPhone> {
//                LoginByPhone { phone ->
//                    navController.navigate(Navigation.SendOTP(phone))
//                }
//            }

            composable<Navigation.SendOTP> {
                VerifyPhoneOPT { navigation ->
                    navController.navigate(navigation)
                }
            }

            composable<Navigation.FailAuth> {
                FailAuth { navigation ->
                    navController.navigate(navigation)
                }
            }

            composable<Navigation.Notes> {
            }

            composable<Navigation.Note> {
            }
        }
    }
}

@Composable
@Preview
fun Home(onNavigate: (Navigation) -> Unit = { }) {
    var email by remember { mutableStateOf("louis.gautier@outlook.fr") }

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier =
                    Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-Mail") },
                    )
                    Button(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(4.dp),
                                ),
                        onClick = { onNavigate(Navigation.SendOTP("")) },
                    ) {
                        Text("Login")
                    }
                }
            }
        },
    )
}

@Composable
fun LoginByPhone(onSend: (String) -> Unit) {
}

@Composable
fun VerifyPhoneOPT(onNavigate: (Navigation) -> Unit) {
}

@Composable
fun FailAuth(onNavigate: (Navigation) -> Unit) {
}
