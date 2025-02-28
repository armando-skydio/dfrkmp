package com.skydio.graphql.domain

import com.skydio.graphql.FetchDroneSenseMissionsMutation.FetchDronesenseMissions
import com.skydio.graphql.source.DroneSenseSource
import com.skydio.platform.Result
import javax.inject.Inject

class FetchDroneSenseMissionsUseCase @Inject constructor(
    private val droneSenseSource: DroneSenseSource,
    private val getUserNodeIdUseCase: GetUserNodeIdUseCase,
) {

    suspend operator fun invoke(userUuid: String): Result<FetchDronesenseMissions?> = Result {
        val userNodeId = getUserNodeIdUseCase(userUuid).getOrThrow()
        droneSenseSource.fetchMissions(userNodeId)
    }

}
