package com.skydio.graphql.source.missions

import com.apollographql.apollo3.ApolloClient
import com.skydio.graphql.WaypointMissionQuery
import com.skydio.graphql.implementation.exception.QueryException
import com.skydio.graphql.source.missions.transformers.Missions
import com.skydio.graphql.source.missions.transformers.MissionsTransformer
import com.skydio.graphql.source.missions.transformers.nonNullEdges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WaypointMissionSource @Inject internal constructor(
    private val client: ApolloClient,
    private val missionsTransformer: MissionsTransformer
) {

    suspend fun getMissions(orgId: String): Missions? = withContext(Dispatchers.IO) {
        runCatching {
            client.query(WaypointMissionQuery(orgId)).execute().data.run {
                return@runCatching this?.nonNullEdges?.let {
                    missionsTransformer.invoke(it)
                }
            }
        }.getOrElse { exception ->
            throw QueryException(exception)
        }

    }

}
