package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.CreateReadyLinkMutation
import com.skydio.graphql.implementation.exception.QueryException
import com.skydio.util.Report
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReadyLinkSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun createReadyLink(
        organizationNodeId: String,
        vehicleId: String,
        expiresAt: String,
        name: String,
        startsAt: String,
    ): CreateReadyLinkMutation.Data? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.mutation(
                CreateReadyLinkMutation(
                    organizationNodeId,
                    vehicleId,
                    expiresAt,
                    name,
                    startsAt,
                )).execute().let { response ->
                response.data
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}
