package com.skydio.mpp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skydio.mpp.login.LoginViewModel
import org.koin.compose.koinInject

@Composable
fun LoginView(
    vm: LoginViewModel = koinInject(),
) {

    LaunchedEffect(Unit) {
        vm.checkToken()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = vm.loginState.value) {
            LoginState.Code, LoginState.ErrorCode, LoginState.LoadingCode -> {
                LoginCodeUi(state)
            }
            LoginState.Email, LoginState.ErrorEmail, LoginState.LoadingEmail -> {
                LoginEmailUi(state)
            }
            is LoginState.Success -> {
                LoginSuccess(state.token)
            }
        }
    }

}
