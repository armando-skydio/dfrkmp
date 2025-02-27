package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.MobileNetworkTestingMutation
import com.skydio.graphql.implementation.exception.QueryException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CloudApiSource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun startNetworkTest(uuid: String, duration: Int = DEFAULT_TEST_LENGTH): MobileNetworkTestingMutation.Data? = withContext(
        Dispatchers.IO
    ) {
        try {
            client.mutation(MobileNetworkTestingMutation(uuid, duration)).execute().let { response ->
                response.data
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }

    companion object {
        val DEFAULT_TEST_LENGTH = 10
    }
}
