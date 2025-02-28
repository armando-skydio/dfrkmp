package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.implementation.exception.QueryException
import com.skydio.graphql.OrganizationQuery
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrganizationNodeIdSource @Inject constructor(
    private val client: ApolloClient
) {
    suspend fun getOrganizationNodeId(orgUuid: String): OrganizationQuery.Organization = withContext(
        Dispatchers.IO
    ) {
        try {
            client.query(OrganizationQuery(orgUuid)).execute().let { response ->
                response.data?.organization
            } ?: throw QueryException("Organization NodeID Not Found")
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}
