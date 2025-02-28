package com.skydio.mpp.login.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CAAuthResponseWrapper(
    @SerialName("data") val data: CAAuthResponse
)

@Serializable
data class CAAuthResponse(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("refresh_expiration") val refreshExpiration: Double = 0.0,
    @SerialName("first_successful_auth") val firstSuccessfulAuth: Boolean = false,
//    val user: CAUser? = null
)
