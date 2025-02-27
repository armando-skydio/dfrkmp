package com.skydio.graphql.domain

import com.skydio.graphql.OrganizationQuery
import com.skydio.graphql.source.OrganizationNodeIdSource
import com.skydio.platform.Result
import javax.inject.Inject

class GetOrganizationNodeIdUseCase @Inject constructor(
    private val organizationNodeIdSource: OrganizationNodeIdSource
) {
    suspend operator fun invoke(orgUuid: String): Result<OrganizationQuery.Organization> {
        return Result {
            organizationNodeIdSource.getOrganizationNodeId(orgUuid)
        }
    }
}
