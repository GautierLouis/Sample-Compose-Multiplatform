package com.louisgautier.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.louisgautier.designsystem.AppTheme
import com.louisgautier.designsystem.theme.button.Button
import com.louisgautier.designsystem.theme.button.ButtonType
import com.louisgautier.designsystem.theme.button.ButtonVariant
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onSuccessfulLogin: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val isMailValid by viewModel.emailCheckState.collectAsStateWithLifecycle()
    val loginState by viewModel.loginState.collectAsStateWithLifecycle(LoginViewModel.LoginState.Idle)

    var email by rememberSaveable { mutableStateOf("louis.gautier@outlook.fr") }
    var pwd by rememberSaveable { mutableStateOf("test") }

    val isLoading = loginState is LoginViewModel.LoginState.Loading
    val loginBtnLabel = if (isLoading) "Loading" else "Login"
    val loginBtnEnabled = pwd.isNotBlank() && isMailValid && !isLoading

    //For Debug only (since email && pwd are pre-filled)
    LaunchedEffect(Unit) {
        if (email.isNotEmpty()) {
            viewModel.checkEmail(email)
        }
    }

    LaunchedEffect(loginState) {
        when (val currentLoginState = loginState) {
            LoginViewModel.LoginState.Success -> {
                onSuccessfulLogin()
            }

            is LoginViewModel.LoginState.Error -> {
                snackbarHostState.showSnackbar(
                    message = getString(currentLoginState.key),
                    duration = SnackbarDuration.Short
                )
            }

            LoginViewModel.LoginState.Loading -> {
                // Button label and enabled state are already handled by derived vals
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = AppTheme.colors.redFamily.fg,
                    contentColor = AppTheme.colors.redFamily.contrast,
                    snackbarData = data
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(50.dp)
                        .height(IntrinsicSize.Min),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = email,
                        onValueChange = {
                            email = it
                            viewModel.checkEmail(it)
                        },
                        label = { Text("E-Mail") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = pwd,
                        onValueChange = { pwd = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 6.dp)
                            .testTag("login"),
                        variant = ButtonVariant.SOLID,
                        type = ButtonType.PRIMARY,
                        enabled = loginBtnEnabled,
                        onClick = {
                            if (loginBtnEnabled) {
                                viewModel.login(email, pwd)
                            }
                        },
                    ) {
                        Text(modifier = Modifier.testTag("btnLabel"), text = loginBtnLabel)
                    }
                }
            }
        }
    )
}
