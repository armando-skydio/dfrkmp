package com.skydio.mpp.login.models

import kotlinx.serialization.Serializable

@Serializable
data class CAAuthRequest(
    var email: String,
    var loginCode: Int,
    var deviceId: String,
    var clientKey: String
)
