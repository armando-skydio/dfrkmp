package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.AppMediaQuery
import com.skydio.graphql.implementation.exception.QueryException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun getMediaFeed(orgId: String): AppMediaQuery.Flights? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.query(AppMediaQuery(orgId)).execute().let { response ->
                response.data?.organization?.flights
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}
