package com.skydio.graphql.source

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Input
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.exception.ApolloException
import com.skydio.graphql.SetVehicleDesiredStateMutation
import com.skydio.graphql.implementation.exception.QueryException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DesiredStateMutation @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun setDesiredState(deviceId: String, id: String, webrtcDesired: Boolean, videoStream: Boolean, webTunnel: Boolean, force: Boolean): SetVehicleDesiredStateMutation.SignalProxyCredentials? = withContext(
            Dispatchers.IO
    ) {
        try {
            client.mutate(SetVehicleDesiredStateMutation(
                    device_id = Input.fromNullable(deviceId),
                    vehicle_node_id = id,
                    webrtc_desired = Input.fromNullable(videoStream),
                    video_stream_desired = Input.fromNullable(videoStream),
                    web_tunnel_desired = Input.fromNullable(webTunnel),
                    force = Input.fromNullable(force))).fetchPolicy(FetchPolicy.NetworkOnly)
                    .execute().let { response ->
                response?.data?.setVehicleDesiredState?.vehicle?.remoteStreaming?.signalProxyCredentials
            }
        } catch (exception: ApolloException) {
            throw QueryException(exception)
        }
    }
}
