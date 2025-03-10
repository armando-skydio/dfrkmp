package com.skydio.mpp.login

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydio.mpp.AUTH_REFRESH_KEY
import com.skydio.mpp.AUTH_TOKEN_KEY
import com.skydio.mpp.DataStoreSingleton
import com.skydio.mpp.api.SkydioApi
import com.skydio.mpp.login.models.CAAuthRequest
import com.skydio.mpp.login.models.CAAuthResponse
import com.skydio.mpp.ui.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class LoginViewModel(val skydioApi: SkydioApi) : ViewModel() {

    val loginState = mutableStateOf<LoginState>(LoginState.Email)
    private var emailId = ""

    init {
        refreshToken()
        viewModelScope.launch {
            //val apollot = skydioApi.cloudSimulatorQuery("4b30cd79-ba46-41b0-a770-cbe9bbaa01fe").execute()
            //println("apollodata ${apollot.data}")
        }
    }

    val tokenFlow: Flow<String> = DataStoreSingleton.getDataStore().data.map { preferences ->
        preferences[AUTH_TOKEN_KEY] ?: ""
    }

    suspend fun checkToken() {
        DataStoreSingleton.getDataStore().data.map { preferences ->
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
        //skydioApi.waypointMissionQuery("4b30cd79-ba46-41b0-a770-cbe9bbaa01fe")
    }

    private fun getDataStore() = DataStoreSingleton.getDataStore()

    private fun refreshToken() = viewModelScope.launch {
        println("Refreshing token")
        while (true) {
            val api = API()
            val refreshToken = getDataStore().data.map { preferences ->
                preferences[AUTH_REFRESH_KEY] ?: ""
            }.first()
            if (refreshToken.isEmpty()) {
                continue
            }
            val refreshedToken = api.refreshToken(refreshToken)
            refreshedToken?.let { refreshResponse ->
                refreshResponse.accessToken
                val ds = getDataStore()
                ds.edit {
                    it[AUTH_TOKEN_KEY] = refreshResponse.accessToken
                }
                println(refreshResponse)
            } ?: kotlin.run {
                println("Refresh token failed")
            }
            delay(5.minutes)
        }
    }

}
