package com.skydio.graphql.domain

import com.skydio.graphql.SendDroneSenseMissionSelectMutation.SendDronesenseMissionSelect
import com.skydio.graphql.source.DroneSenseSource
import com.skydio.platform.Result
import javax.inject.Inject

class SelectDroneSenseMissionUseCase @Inject constructor(
    private val droneSenseSource: DroneSenseSource,
    private val getUserNodeIdUseCase: GetUserNodeIdUseCase,
) {

    suspend operator fun invoke(
        userUuid: String,
        callSign: String,
        missionId: String,
        flightId: String,
        vehicleSerial: String,
    ): Result<SendDronesenseMissionSelect?> = Result {
        val userNodeId = getUserNodeIdUseCase(userUuid).getOrThrow()
        droneSenseSource.selectMission(userNodeId, callSign, missionId, flightId, vehicleSerial)
    }

}
