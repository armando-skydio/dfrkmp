package com.skydio.graphql.domain

import com.skydio.graphql.source.UserNodeIdSource
import com.skydio.platform.Result
import javax.inject.Inject

class GetUserNodeIdUseCase @Inject constructor(
    private val userNodeIdSource: UserNodeIdSource
) {
    suspend operator fun invoke(userUuid: String): Result<String> =
        Result { userNodeIdSource.getUserNodeId(userUuid) }
}
