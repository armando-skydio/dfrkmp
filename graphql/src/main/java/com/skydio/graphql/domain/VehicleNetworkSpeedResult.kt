package com.skydio.graphql.domain

import com.google.gson.Gson
import com.skydio.graphql.data.SpeedTestResult
import com.skydio.graphql.source.VehicleSource
import com.skydio.platform.Result
import com.skydio.platform.exception.NoDataException
import java.lang.Exception
import javax.inject.Inject

class VehicleNetworkSpeedResult @Inject constructor(
    private val vehicleSource: VehicleSource,
    private val gson: Gson
) {
    suspend operator fun invoke(vehicleId: String): Result<SpeedTestResult> {
        return Result {
            vehicleSource.getNetworkTestResult(vehicleId)?.let {
                // TODO(armando) - We might want to list, but we don't have UI definition yet
                val resultString = it.results.last()?.skyperfResult?.jsonResult ?: throw NoDataException("Vehicle speed test result not available")
                try {
                    gson.fromJson(resultString, SpeedTestResult::class.java)
                } catch (exception: Exception) {
                    throw NoDataException("Failure to parse result")
                }
            } ?: throw NoDataException("Vehicle speed test result not available")
        }
    }

    companion object {
        private const val TAG = "VehicleNetworkSpeedRequest"
    }
}