package com.skydio.mpp.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginView(
    vm: LoginViewModel = viewModel { LoginViewModel() },
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.checkToken(context)
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
