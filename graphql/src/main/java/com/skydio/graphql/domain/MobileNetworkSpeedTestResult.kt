package com.skydio.graphql.domain

import com.google.gson.Gson
import com.skydio.graphql.data.SpeedTestResult
import com.skydio.platform.Result
import com.skydio.platform.exception.NoDataException
import java.lang.Exception
import javax.inject.Inject

class MobileNetworkSpeedTestResult @Inject constructor(
    private val gson: Gson
) {
    suspend operator fun invoke(vehicleId: String): Result<SpeedTestResult> {
        return Result {
            val resultString = ""
            try {
                gson.fromJson(resultString, SpeedTestResult::class.java)
            } catch (exception: Exception) {
                throw NoDataException("Failure to parse result")
            }
        }
    }

    companion object {
        private const val TAG = "VehicleNetworkSpeedRequest"
    }
}