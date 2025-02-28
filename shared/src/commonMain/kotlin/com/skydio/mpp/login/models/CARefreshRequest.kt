package com.skydio.mpp.login.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CARefreshRequest(
    @SerialName("client_key") var clientKey: String
)
