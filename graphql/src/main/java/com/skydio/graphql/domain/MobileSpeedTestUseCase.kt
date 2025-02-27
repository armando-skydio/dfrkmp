package com.skydio.graphql.domain

import com.skydio.djinni.platform.PlatformInfo
import com.skydio.graphql.MobileNetworkTestingMutation
import com.skydio.platform.Result
import javax.inject.Inject

class MobileSpeedTestUseCase @Inject constructor(
    private val vehicleNetworkSpeedRequest: VehicleNetworkSpeedRequest,
    private val vehicleNetworkSpeedResult: VehicleNetworkSpeedResult,
    private val platformInfo: PlatformInfo
) {

    suspend operator fun invoke(session: MobileNetworkTestingMutation.Session): Result<String> =
        Result {
            ""
        }

}