package com.skydio.graphql.implementation

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.JsonParser
import com.skydio.cloud.CloudException
import com.skydio.cloud.data.authentication.CloudApiAuthzInterceptor
import com.skydio.cloud.data.authentication.CloudApiAuthzInterceptor.Companion.KEY_AUTH_TOKEN
import com.skydio.cloud.data.authentication.CloudApiAuthzInterceptor.Companion.KEY_REFRESH_TOKEN
import com.skydio.cloud.data.authentication.RefreshTokenUseCase
import com.skydio.util.Report
import okhttp3.Interceptor
import okhttp3.Response

internal class GenericInterceptor(
    private val authorizationHeaderFactory: HeaderFactory,
    private val sharedPreferences: SharedPreferences,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val cloudClientKey: String,
) : Interceptor {

    companion object {
        private const val TAG = "GenericInterceptor"
        private const val REFRESH_REQUIRED = 3100
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.header("Content-Type", "application/json")
        requestBuilder.header("Accept", "application/json")
        requestBuilder.header(
            "Authorization",
            "Bearer ${sharedPreferences.getString(CloudApiAuthzInterceptor.KEY_AUTH_TOKEN, "")}"
        )
        requestBuilder.header(
            "X-Client-Key",
            cloudClientKey,
        )
        authorizationHeaderFactory.getAuthHeaders().forEach { header ->
            requestBuilder.header(header.first, header.second)
        }

        var response = chain.proceed(requestBuilder.build())

        // NOTE: We can only access the responseBody only one time!
        val responseBodyString = response.peekBody(Long.MAX_VALUE).string()

        // Parse to JSON object
        val jsonObject = JsonParser.parseString(responseBodyString).asJsonObject
        val code = jsonObject.get("code")?.asInt

        // check for 3100 error and refresh token
        // TODO(anil): Move to standard error codes when cloud does
        code?.let {
            if (it == REFRESH_REQUIRED) {
                Log.v(TAG, "Refreshing Token!")
                val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, "")

                try {
                    refreshTokenUseCase(refreshToken ?: "")
                    requestBuilder.removeHeader("Authorization")
                    requestBuilder.header(
                        "Authorization",
                        "Bearer ${sharedPreferences.getString(KEY_AUTH_TOKEN, "")}"
                    )
                    response = chain.proceed(requestBuilder.build())
                } catch (ex: CloudException) {
                    Report.error("AccessToken auth refresh failed", ex)
                }
            }
        }

        return response
    }
}
