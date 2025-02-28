package com.skydio.graphql.domain

import com.skydio.graphql.source.VehicleSource
import com.skydio.graphql.type.vehicletype
import com.skydio.platform.Result
import javax.inject.Inject

class GetIotVehicleByIdUseCase @Inject constructor(
    private val vehicleSource: VehicleSource
) {
    suspend operator fun invoke(vehicleId: String): Result<IotVehicle?> {
        return Result {
            vehicleSource.getVehicleById(vehicleId)?.let {
                IotVehicle(
                    it.id,
                    it.displayName,
                    it.vehicleId,
                    it.vehicleType,
                    it.isOnline ?: false,
                    it.canTeleop ?: false,
                    it.uuid,
                    it.organizationId ?: "",
                    it.remoteStreaming?.viewerCount ?: 0
                )
            }
        }
    }

    data class IotVehicle(
        val graphQlId: String,
        val displayName: String,
        val vehicleId: String,
        val vehicleType: vehicletype,
        val isOnline: Boolean,
        val teleopEnabled: Boolean,
        val vehicleUuid: String,
        val organizationId: String,
        val viewerCount: Int,
    )

    companion object {
        private const val TAG = "GetIotVehicleByIdUseCase"
    }
}
