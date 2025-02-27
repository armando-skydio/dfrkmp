package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.implementation.exception.QueryException
import com.skydio.graphql.OrganizationQuery
import com.skydio.graphql.UserQuery
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserNodeIdSource @Inject constructor(
    private val client: ApolloClient
) {
    suspend fun getUserNodeId(userUuid: String): String = withContext(Dispatchers.IO) {
        try {
            client.query(UserQuery(userUuid)).execute().let { response ->
                response.data?.nodeByUuid?.onUser?.id
            } ?: throw QueryException("User NodeID Not Found")
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}
