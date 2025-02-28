package com.skydio.mpp.login.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CARefreshResponseWrapper(
    @SerialName("data") val data: CARefreshResponse
)

@Serializable
data class CARefreshResponse(
    @SerialName("access_token") var accessToken: String
)
