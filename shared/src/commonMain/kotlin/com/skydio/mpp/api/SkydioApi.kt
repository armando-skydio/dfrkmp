package com.skydio.mpp.api

import com.apollographql.apollo3.ApolloCall
import com.skydio.graphql.CloudSimulatorsScreenQuery
import com.skydio.graphql.WaypointMissionQuery

interface SkydioApi {

    suspend fun waypointMissionQuery(orgId: String): ApolloCall<WaypointMissionQuery.Data>

    suspend fun cloudSimulatorQuery(orgId: String): ApolloCall<CloudSimulatorsScreenQuery.Data>
}