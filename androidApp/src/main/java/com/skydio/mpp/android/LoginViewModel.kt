package com.skydio.mpp.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydio.mpp.login.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    val loginState = mutableStateOf<LoginState>(LoginState.Email)

    fun requestLoginCode(email: String) = viewModelScope.launch(Dispatchers.IO) {
        loginState.value = LoginState.LoadingEmail
        val api = API()
        val res = api.requestLoginCode(email)
        println(res)
        if (res.status.value in 200 .. 299) {
            loginState.value = LoginState.Code
        } else {
            loginState.value = LoginState.ErrorEmail
        }
    }

}
