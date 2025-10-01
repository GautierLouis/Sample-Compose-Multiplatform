package com.louisgautier.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.louisgautier.auth.AuthRepository
import com.louisgautier.utils.AppErrorCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    sealed class LoginState {
        data object Idle : LoginState()
        data object Success : LoginState()
        data object Loading : LoginState()
        data class Error(val key: StringResource) : LoginState()
    }

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    var loginState = MutableStateFlow<LoginState>(LoginState.Idle)
        private set

    var emailCheckState = MutableStateFlow(false)
        private set

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            loginState.emit(LoginState.Loading)
            authRepository.login(email, password)
                .onSuccess {
                    loginState.emit(LoginState.Success)
                }
                .onFailure { throwable ->
                    val appCode = (throwable as? AppErrorCode) ?: AppErrorCode.UnknownError
                    loginState.emit(LoginState.Error(appCode.toResourceString()))
                }
        }
    }

    fun checkEmail(email: String) {
        val match = email.matches(emailRegex)
        emailCheckState.update { match }
    }
}