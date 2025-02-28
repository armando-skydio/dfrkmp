package com.skydio.graphql.domain

import com.skydio.graphql.implementation.exception.InvalidResultException
import com.skydio.graphql.source.DesiredStateMutation
import com.skydio.platform.exception.NoDataException
import javax.inject.Inject
import com.skydio.platform.Result

class SetIotVehicleTeleopUseCase @Inject constructor(
    private val setVehicleDesiredState: DesiredStateMutation,
) {

    data class SignalProxyCredential(val url: String, val jwt: String)

    suspend operator fun invoke(deviceId: String, vehicleId: String, enabled: Boolean) =
        Result {
            val credentials = setVehicleDesiredState.setDesiredState(
                deviceId.lowercase(),
                vehicleId,
                enabled,
                enabled,
                enabled,
                true
            )
            when {
                credentials == null -> throw NoDataException("Cloud backend return Null credentials")
                credentials.token.isNullOrEmpty() || credentials.url.isNullOrEmpty() -> throw InvalidResultException("Credentials invalid token='${credentials.token}' url='${credentials.url}'")
                else -> SignalProxyCredential(credentials.url ?: "", credentials.token ?: "")
            }
        }

}
