package com.skydio.graphql.domain

import com.skydio.graphql.source.VehicleSource
import com.skydio.platform.Result
import javax.inject.Inject

class VehicleNetworkSpeedRequest @Inject constructor(
    private val vehicleSource: VehicleSource
) {
    suspend operator fun invoke(uuid: String): Result<Boolean> {
        return Result {
            vehicleSource.startNetworkTest(uuid)?.let {
                true
            } ?: false
        }
    }

    companion object {
        private const val TAG = "VehicleNetworkSpeedRequest"
    }
}