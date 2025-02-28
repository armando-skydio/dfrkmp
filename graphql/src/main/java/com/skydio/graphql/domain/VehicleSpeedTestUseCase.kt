package com.skydio.graphql.domain

import com.skydio.extensions.round
import com.skydio.graphql.implementation.exception.InvalidResultException
import com.skydio.graphql.source.VehicleSource
import com.skydio.platform.Result
import com.skydio.platform.exception.NoDataException
import kotlinx.coroutines.delay
import javax.inject.Inject

class VehicleSpeedTestUseCase @Inject constructor(
    private val getIotVehicleByIdUseCase: GetIotVehicleByIdUseCase,
    private val vehicleNetworkSpeedRequest: VehicleNetworkSpeedRequest,
    private val vehicleNetworkSpeedResult: VehicleNetworkSpeedResult
) {

    suspend operator fun invoke(vehicleId: String): Result<String> =
        Result {
            // NOTE(armando) - 1. First get vehicle
            when(val vehicle = getIotVehicleByIdUseCase(vehicleId)) {
                is Result.Success -> {
                    // NOTE(armando) - 2. Start speed request
                    when (vehicleNetworkSpeedRequest(vehicle.data?.graphQlId ?: "")) {
                        is Result.Success -> {
                            // NOTE(armando) - 3. Wait for 5 seconds more than the test
                            delay((VehicleSource.DEFAULT_TEST_LENGTH + 5L) * 1000)
                            // NOTE(armando) - 4. Get test results
                            when (val result = vehicleNetworkSpeedResult(vehicleId)) {
                                is Result.Success -> {
                                    var total = 0.0f
                                    var resultCount = 0
                                    result.data.intervals?.forEach {
                                        resultCount ++
                                        total += (it.streams?.get(0)?.bits_per_second ?: 0f) / 1000000
                                    }
                                    if (resultCount > 0) {
                                        val res = (total / resultCount).round(2)
                                        "Average speed ${res}Mbps"
                                    } else {
                                        "No test results found"
                                    }
                                }
                                else -> throw InvalidResultException("Network Request failed to start")
                            }
                        }
                        else -> throw InvalidResultException("No vehicle id found")
                    }
                }
                else -> throw NoDataException("No vehicle id found")
            }
        }

}