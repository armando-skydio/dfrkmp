package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.ControllerQuery
import com.skydio.graphql.implementation.exception.QueryException
import com.skydio.platform.exception.NoDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ControllerSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun getController(serial: String): ControllerQuery.Controller? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.query(ControllerQuery(serial = serial)).fetchPolicy(
                FetchPolicy.NetworkOnly
            ).execute().let { response ->
                response.data?.controller
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}
