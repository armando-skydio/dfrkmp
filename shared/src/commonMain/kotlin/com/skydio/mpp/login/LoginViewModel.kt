package com.skydio.mpp.login

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydio.mpp.AUTH_REFRESH_KEY
import com.skydio.mpp.AUTH_TOKEN_KEY
import com.skydio.mpp.DataStoreMaker
import com.skydio.mpp.login.models.CAAuthRequest
import com.skydio.mpp.login.models.CAAuthResponse
import com.skydio.mpp.ui.LoginState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val loginState = mutableStateOf<LoginState>(LoginState.Email)
    private var emailId = ""

    private fun getDataStore(): DataStore<Preferences>  = DataStoreMaker.make()

    suspend fun checkToken() {
        getDataStore().data.map { preferences ->
            preferences[AUTH_TOKEN_KEY] ?: ""
        }.collectLatest {
            if (it.isNotEmpty()) {
                loginState.value = LoginState.Success(it)
            }
        }
    }

    fun requestLoginCode(email: String) = viewModelScope.launch() {
        loginState.value = LoginState.LoadingEmail
        val api = API()
        val res = api.requestLoginCode(email)
        if (res?.status?.value in 200..299) {
            loginState.value = LoginState.Code
            emailId = email
        } else {
            loginState.value = LoginState.ErrorEmail
        }
    }

    fun verifyLoginCode(
        code: String
    ) = viewModelScope.launch() {
        loginState.value = LoginState.LoadingCode
        val parsedCode = runCatching {
            code.toInt()
        }.getOrElse {
            loginState.value = LoginState.ErrorCode
            return@launch
        }
        val api = API()
        val res: CAAuthResponse = api.authenticate(
            CAAuthRequest(
                email = emailId,
                loginCode = parsedCode,
                deviceId = "Spike-409",
                clientKey = API.CLIENT_KEY
            )
        ) ?: kotlin.run {
            loginState.value = LoginState.ErrorCode
            return@launch
        }
        loginState.value = LoginState.Success(res.accessToken)
        val ds = getDataStore()
        ds.edit {
            it[AUTH_TOKEN_KEY] = res.accessToken
            it[AUTH_REFRESH_KEY] = res.refreshToken
        }
    }

}
