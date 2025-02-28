package com.skydio.graphql.domain

import com.skydio.cloud.domain.GetUserInfoUseCase
import com.skydio.djinni.platform.PlatformInfo
import com.skydio.graphql.mutations.ClaimControllerMutation
import com.skydio.platform.Result
import java.lang.Exception
import javax.inject.Inject

class ClaimControllerUseCase @Inject constructor(
    private val claimControllerMutation: ClaimControllerMutation,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getOrganizationNodeIdUseCase: GetOrganizationNodeIdUseCase,
    private val platformInfo: PlatformInfo,
) {
    suspend operator fun invoke(): Result<Boolean> {
        val id = when (val orgId = getOrganizationNodeIdUseCase(getUserInfoUseCase().organizationId ?: "")) {
            is Result.Success -> orgId.data.id
            else -> ""
        }
        if (id.isEmpty()) {
            return Result {
                throw Exception("No Organization ID")
            }
        }
        return Result {
            claimControllerMutation.createSimulator(id, platformInfo.clientId)
        }
    }
}