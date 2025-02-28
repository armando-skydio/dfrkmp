package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.skydio.graphql.DeviceNetworkTestingMutation
import com.skydio.graphql.DeviceNetworkTestingResultsQuery
import com.skydio.graphql.VehicleHeaderQuery
import com.skydio.graphql.VehicleScreenQuery
import com.skydio.graphql.implementation.exception.QueryException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VehicleSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun getVehicleList(
        orgId: String,
        pageSize: Int,
    ): List<VehicleScreenQuery.Node> = withContext(
        Dispatchers.IO
    ) {
        try {
            val vehicleList = mutableListOf<VehicleScreenQuery.Node>()
            // NOTE(bojanin): FetchPolicy.NetworkOnly so that apollo doesnt cache the result from app launch. We need vehicle updates dynamically.
            // i.e. battery status, online state, etc
            var hasNext = true
            var nextCursor: String? = null
            while (hasNext) {
                client.query(VehicleScreenQuery(
                    orgid = orgId,
                    lastKnownLocation = true,
                    pageSize = pageSize,
                    onlineVehiclesOnly = true,
                    after = Optional.presentIfNotNull(nextCursor),
                )).fetchPolicy(FetchPolicy.NetworkOnly).execute().let { response ->
                    hasNext = response.data?.organization?.vehicles?.pageInfo?.hasNextPage ?: false
                    nextCursor = response.data?.organization?.vehicles?.pageInfo?.endCursor
                    response.data?.organization?.vehicles?.edges?.mapNotNull {
                        it?.node
                    }?.let {
                        vehicleList.addAll(it)
                    }
                }
            }
            vehicleList
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }

    suspend fun getVehicleById(vehicleId: String): VehicleHeaderQuery.Vehicle? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.query(VehicleHeaderQuery(vehicleId)).execute().let { response ->
                response.data?.vehicle
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }

    suspend fun startNetworkTest(uuid: String, duration: Int = DEFAULT_TEST_LENGTH): DeviceNetworkTestingMutation.Data? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.mutation(DeviceNetworkTestingMutation(uuid, duration)).fetchPolicy(FetchPolicy.NetworkOnly).execute().let { response ->
                response.data
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }

    suspend fun getNetworkTestResult(vehicleId: String): DeviceNetworkTestingResultsQuery.DeviceNetworkTestResults? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.query(DeviceNetworkTestingResultsQuery(vehicleId)).fetchPolicy(FetchPolicy.NetworkOnly).execute().let { response ->
                response.data?.vehicle?.deviceNetworkTestResults
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }

    companion object {
        val DEFAULT_TEST_LENGTH = 10
    }
}
