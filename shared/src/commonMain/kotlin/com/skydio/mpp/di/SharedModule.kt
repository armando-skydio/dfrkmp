package com.skydio.mpp.di

import com.apollographql.apollo3.ApolloClient
import org.koin.core.context.startKoin
import org.koin.dsl.module
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.skydio.mpp.api.SkydioApi
import com.skydio.mpp.api.SkydioService
import com.skydio.mpp.repository.WaypointsRepository
import kotlinx.coroutines.CoroutineDispatcher

private val dataModule = module {
    // Services
    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl("https://api.skydio.com/graphql")
            .normalizedCache(MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024))
            .build()
    }
    single<SkydioApi> { SkydioService(get()) }

    // Repositories
    single<WaypointsRepository> { WaypointsRepository(get()) }
}

private val domainModule = module {

}

private val sharedModule = listOf(dataModule,  domainModule)

fun initKoin() {
    startKoin {
        modules(sharedModule)
    }
}