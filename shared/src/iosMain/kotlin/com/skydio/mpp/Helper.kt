package com.skydio.mpp

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.skydio.mpp.api.SkydioApi
import com.skydio.mpp.api.SkydioService
import com.skydio.mpp.login.LoginViewModel
import com.skydio.mpp.repository.WaypointsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

suspend fun token(): Flow<String> {
    return DataStoreSingleton.getDataStore().data.map { preferences ->
        preferences[AUTH_TOKEN_KEY] ?: ""
    }
}

private val dataModule = module {

    // Services
    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl("https://api.skydio.com/graphql")
            .addHttpInterceptor(object : HttpInterceptor {
                override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
                    var token  = ""
                    runBlocking {
                        token = token().first()
                    }
                    val newRequest = request.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    return chain.proceed(newRequest)
                }
            })
            .normalizedCache(MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024))
            .build()
    }
    single<SkydioApi> { SkydioService(get()) }

    // Repositories
    single<WaypointsRepository> { WaypointsRepository(get()) }
}

private val domainModule = module {

}

private val vmModule = module {
    single { LoginViewModel(get()) }
}
private val sharedModule = listOf(dataModule,  domainModule, vmModule)


fun initKoin() =
    startKoin {
        modules(
            sharedModule
        )
    }

