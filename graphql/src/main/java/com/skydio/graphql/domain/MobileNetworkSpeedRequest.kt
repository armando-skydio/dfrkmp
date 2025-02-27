package com.skydio.graphql.domain

import com.skydio.cloud.data.DeviceId
import com.skydio.graphql.MobileNetworkTestingMutation
import com.skydio.graphql.source.CloudApiSource
import com.skydio.platform.Result
import javax.inject.Inject

class MobileNetworkSpeedRequest @Inject constructor(
    private val cloudApiSource: CloudApiSource,
    @DeviceId val deviceId: String
) {
    suspend operator fun invoke(): Result<MobileNetworkTestingMutation.Session?> {
        return Result {
            cloudApiSource.startNetworkTest(deviceId)?.let {
                it.requestNetworkTest?.testSuite?.tests?.get(0)?.skyperf?.sessions?.get(0)
            }
        }
    }

    companion object {
        private const val TAG = "MobileNetworkSpeedRequest"
    }
}