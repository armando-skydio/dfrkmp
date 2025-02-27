package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.CloudSimulatorsScreenQuery
import com.skydio.graphql.implementation.exception.QueryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CloudSimulatorsSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun getSimulators(branch : String = "master"): CloudSimulatorsScreenQuery.SimulatorContainerVersions? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.query(CloudSimulatorsScreenQuery(branch)).execute().let { response ->
                response.data?.simulatorContainerVersions
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}