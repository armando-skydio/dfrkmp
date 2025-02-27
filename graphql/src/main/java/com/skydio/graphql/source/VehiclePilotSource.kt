package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.VehicleQuery
import com.skydio.graphql.implementation.exception.QueryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VehiclePilotSource @Inject constructor(
    private val client: ApolloClient
) {
    suspend fun getVehicleCurrentPilot(vehicleId: String): VehicleQuery.CurrentPilot? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.query(VehicleQuery(vehicleId)).execute().let { response ->
                response.data?.vehicle?.currentPilot
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}