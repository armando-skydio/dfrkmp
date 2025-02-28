package com.skydio.mpp.login.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CAAuthRequest(
    var email: String,
    @SerialName("login_code") var loginCode: Int,
    @SerialName("device_id") var deviceId: String,
    @SerialName("client_key") var clientKey: String
)
