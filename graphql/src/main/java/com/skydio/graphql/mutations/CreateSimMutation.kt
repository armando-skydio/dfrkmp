package com.skydio.graphql.mutations

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.CreateSimulatorMutation
import com.skydio.graphql.implementation.exception.QueryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateSimMutation @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun createSimulator(name: String, orgUuid: String, version: String): CreateSimulatorMutation.Vehicle? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.mutate(
                CreateSimulatorMutation(
                    name,
                    orgUuid,
                    version,
                    true,
                    true
                )
            ).fetchPolicy(FetchPolicy.NetworkOnly)
                .execute().let { response ->
                    response.data?.createCloudSimulator?.vehicle
                }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}
