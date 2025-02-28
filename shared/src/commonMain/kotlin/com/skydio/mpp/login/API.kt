package com.skydio.mpp.login

import com.skydio.mpp.login.models.CAAuthRequest
import com.skydio.mpp.login.models.CAAuthResponse
import com.skydio.mpp.login.models.CAAuthResponseWrapper
import com.skydio.mpp.login.models.CALoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class API {

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }


    suspend fun requestLoginCode(
        email: String,
        clientKey: String = "e181e9c26ac1c98aa478d5716479edd9d75b38dffc50d580fcf10f772c0d0a25"
    ): HttpResponse {
        val response: HttpResponse = client.post({
            url {
                protocol = URLProtocol.HTTPS
                host = "api.skydio.com"
                path("auth/login")
            }
            headers {
                append("X-Endpoint-Flag", "no-auth")
            }
            contentType(ContentType.Application.Json)
            setBody(CALoginRequest(email, clientKey))
        })
        println(response.bodyAsText())
        return response
    }

    suspend fun authenticate(request: CAAuthRequest): CAAuthResponse {
        val response = client.post({
            url {
                protocol = URLProtocol.HTTPS
                host = "api.skydio.com"
                path("auth/authenticate")
            }
            headers {
                append("X-Endpoint-Flag", "no-auth")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        })
        println(response.bodyAsText())
        val data: CAAuthResponseWrapper = response.body()
        return data.data
    }
}
