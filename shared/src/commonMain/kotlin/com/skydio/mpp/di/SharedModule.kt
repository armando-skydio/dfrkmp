package com.skydio.mpp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import org.koin.core.context.startKoin
import org.koin.dsl.module
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.skydio.mpp.AUTH_REFRESH_KEY
import com.skydio.mpp.AUTH_TOKEN_KEY
import com.skydio.mpp.DataStoreMaker
import com.skydio.mpp.DataStoreSingleton
import com.skydio.mpp.api.SkydioApi
import com.skydio.mpp.api.SkydioService
import com.skydio.mpp.login.LoginViewModel
import com.skydio.mpp.repository.WaypointsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration

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
val sharedModule = listOf(dataModule,  domainModule, vmModule)

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            sharedModule
        )
    }
