package com.skydio.graphql.domain

import com.skydio.graphql.source.VehiclePilotSource
import com.skydio.platform.Result
import javax.inject.Inject

class GetVehiclePilotUseCase @Inject constructor(private val vehiclePilotSource: VehiclePilotSource) {
    suspend operator fun invoke(vehicleId: String): Result<CurrentPilot> {
        return Result {
            val result = vehiclePilotSource.getVehicleCurrentPilot(vehicleId)
            CurrentPilot(result?.displayName, result?.email)
        }
    }

    data class CurrentPilot(val displayName: String?, val email: String?)

    companion object {
        private const val TAG = "GetVehiclePilotUseCase"
    }
}