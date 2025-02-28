package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.FetchDroneSenseMissionsMutation
import com.skydio.graphql.FetchDroneSenseMissionsMutation.FetchDronesenseMissions
import com.skydio.graphql.SendDroneSenseMissionSelectMutation
import com.skydio.graphql.SendDroneSenseMissionSelectMutation.SendDronesenseMissionSelect
import com.skydio.graphql.implementation.exception.QueryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DroneSenseSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun fetchMissions(
        userNodeId: String
    ): FetchDronesenseMissions? = withContext(Dispatchers.IO) {
        try {
            client.mutation(FetchDroneSenseMissionsMutation(userNodeId)).execute()
                .let { response -> response.data?.fetchDronesenseMissions }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }

    suspend fun selectMission(
        userNodeId: String,
        callSign: String,
        missionId: String,
        flightId: String,
        vehicleSerial: String,
    ): SendDronesenseMissionSelect? = withContext(Dispatchers.IO) {
        try {
            client.mutation(
                SendDroneSenseMissionSelectMutation(
                    userNodeId,
                    flightId,
                    missionId,
                    callSign,
                    vehicleSerial
                )
            )
                .execute()
                .let { response -> response.data?.sendDronesenseMissionSelect }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }

}