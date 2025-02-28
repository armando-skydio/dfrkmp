package com.skydio.graphql.mutations

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.ClaimControllerMutation
import com.skydio.graphql.implementation.exception.QueryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClaimControllerMutation @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun createSimulator(orgUuid: String, serial: String): Boolean = withContext(
        Dispatchers.IO
    ) {
        try {
            client.mutate(
                ClaimControllerMutation(
                    orgUuid,
                    arrayListOf(serial),
                )
            ).fetchPolicy(FetchPolicy.NetworkOnly)
                .execute().let { response ->
                    response.data?.claimDevices?.controllers?.firstOrNull() != null
                }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}