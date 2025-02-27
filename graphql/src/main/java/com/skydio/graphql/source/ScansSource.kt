package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.AppScanQuery
import com.skydio.graphql.implementation.exception.QueryException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScansSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun getScanFeed(orgId: String): AppScanQuery.Scans? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.query(AppScanQuery(orgId)).execute().let { response ->
                response.data?.organization?.scans
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}
