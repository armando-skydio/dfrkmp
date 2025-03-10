package com.skydio.mpp.api

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.skydio.graphql.CloudSimulatorsScreenQuery
import com.skydio.graphql.WaypointMissionQuery

internal class SkydioService(
    private val apolloClient : ApolloClient
) : SkydioApi {

    override suspend fun waypointMissionQuery(orgId: String): ApolloCall<WaypointMissionQuery.Data> {
        return apolloClient.query(WaypointMissionQuery(orgId))
    }

    override suspend fun cloudSimulatorQuery(branch: String): ApolloCall<CloudSimulatorsScreenQuery.Data> {
        return apolloClient.query(CloudSimulatorsScreenQuery("master"))
    }
}