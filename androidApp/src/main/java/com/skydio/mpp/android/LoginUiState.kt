package com.skydio.mpp.android

object LoginUiState {

    var state: LoginState = LoginState.Email

}

sealed interface LoginState {
    object Email : LoginState
    object Code : LoginState
    object ErrorCode : LoginState
    object ErrorEmail : LoginState
    object LoadingEmail : LoginState
    object LoadingCode : LoginState
    object Success : LoginState
}
