package com.louisgautier.composeApp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.louisgautier.composeApp.MainNavigation
import com.louisgautier.designsystem.AppSize
import com.louisgautier.designsystem.theme.button.Button
import com.louisgautier.designsystem.theme.button.ButtonType
import com.louisgautier.designsystem.theme.button.ButtonVariant
import org.jetbrains.compose.resources.stringResource
import sample.client.composeapp.generated.resources.Res
import sample.client.composeapp.generated.resources.greeting

@Composable
fun Home(
    onNavigate: (MainNavigation) -> Unit = { }
) {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(AppSize.size8)
                ) {
                    Button(
                        variant = ButtonVariant.GHOST,
                        type = ButtonType.SECONDARY,
                        enabled = true,
                        onClick = { onNavigate(MainNavigation.Login) },
                    ) {
                        Text(stringResource(Res.string.greeting))
                    }

                    Button(
                        variant = ButtonVariant.GHOST,
                        type = ButtonType.SECONDARY,
                        enabled = true,
                        onClick = { onNavigate(MainNavigation.Gallery) },
                    ) {
                        Text("Go to gallery")
                    }
                }

            }
        }
    )
}
