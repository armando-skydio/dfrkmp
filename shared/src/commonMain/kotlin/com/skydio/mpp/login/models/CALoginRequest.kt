package com.skydio.mpp.login.models

import kotlinx.serialization.Serializable

@Serializable
data class CALoginRequest(
    var email: String,
    var clientKey: String
)
