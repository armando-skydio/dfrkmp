package com.skydio.mpp.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydio.mpp.login.API
import com.skydio.mpp.login.models.CAAuthRequest
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val loginState = mutableStateOf<LoginState>(LoginState.Email)
    private var emailId = ""

    fun requestLoginCode(email: String) = viewModelScope.launch(Dispatchers.IO) {
        loginState.value = LoginState.LoadingEmail
        val api = API()
        val res = api.requestLoginCode(email)
        println(res)
        if (res.status.value in 200..299) {
            loginState.value = LoginState.Code
            emailId = email
        } else {
            loginState.value = LoginState.ErrorEmail
        }
    }

    fun verifyLoginCode(code: String) = viewModelScope.launch(Dispatchers.IO) {
        loginState.value = LoginState.LoadingCode
        val parsedCode = runCatching {
            code.toInt()
        }.getOrElse {
            loginState.value = LoginState.ErrorCode
            return@launch
        }
        val api = API()
        val res = api.authenticate(
            CAAuthRequest(
                email = emailId,
                loginCode = parsedCode,
                deviceId = "Spike-409",
                clientKey = "e181e9c26ac1c98aa478d5716479edd9d75b38dffc50d580fcf10f772c0d0a25"
            )
        )
        println(res)
        if (res.status.value in 200..299) {
            loginState.value = LoginState.Success(res.bodyAsText())
        } else {
            loginState.value = LoginState.ErrorCode
        }
    }

}
