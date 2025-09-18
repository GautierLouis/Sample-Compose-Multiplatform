package com.louisgautier.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.louisgautier.domain.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App(
    repository: AuthRepository = koinInject()
) {

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        var tokens by remember { mutableStateOf(Pair("", "")) }
        LaunchedEffect("getToken") {
            repository.getUserCreds().collect { token ->
                tokens = token
            }
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                scope.launch { repository.login() }
            }) {
                Text("Log me in!")
            }

            Button(onClick = {
                scope.launch { repository.unprotected() }
            }) {
                Text("Access unprotected content")
            }

            Button(onClick = {
                scope.launch { repository.protected() }
            }) {
                Text("Access protected content")
            }

            Text(tokens.first)
            Text(tokens.second)

//            AnimatedVisibility(showContent) {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose:")
//                }
//            }
        }
    }
}