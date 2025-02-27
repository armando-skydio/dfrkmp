package com.skydio.graphql

import android.content.Context
import android.content.SharedPreferences
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.skydio.analytics.AnalyticsEventListenerFactory
import com.skydio.analytics.domain.logging.GetAnalyticsHttpInterceptorsUseCase
import com.skydio.analytics.domain.logging.GetAnalyticsTraceInterceptorUseCase
import com.skydio.cloud.data.CloudClientVersion
import com.skydio.cloud.data.authentication.CloudApiAuthzInterceptor
import com.skydio.cloud.data.authentication.CloudApiCompatibilityUseCase
import com.skydio.cloud.data.authentication.CloudAuthStateManager
import com.skydio.cloud.data.authentication.cloudUrl.CloudUrlUtil
import com.skydio.cloud.data.authentication.RefreshTokenUseCase
import com.skydio.cloud.di.CloudApiSharedPrefs
import com.skydio.di.AppPackageName
import com.skydio.graphql.implementation.ApolloServerUrl
import com.skydio.graphql.implementation.AuthorizationHeaderFactory
import com.skydio.graphql.implementation.GenericInterceptor
import com.skydio.graphql.implementation.HeaderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class GraphQLPrefs

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CloudClientKey

@Module
@Suppress("TooManyFunctions")
@InstallIn(SingletonComponent::class)
class GraphQLModule {
    @Provides
    @ApolloServerUrl
    fun provideServerUrl(cloudUrlUtil: CloudUrlUtil) = "${cloudUrlUtil.getBaseUrl()}/graphql"

    @Provides
    @Singleton
    fun provideGenericInterceptor(): HeaderFactory = AuthorizationHeaderFactory()

    @Singleton
    @Provides
    @GraphQLPrefs
    fun provideSharedPreferences(@ApplicationContext appContext: Context, @AppPackageName packageName: String): SharedPreferences =
        appContext.getSharedPreferences(packageName, Context.MODE_PRIVATE)

    @Provides
    @CloudClientKey
    fun provideCloudClientKey() = BuildConfig.SKYDIO_APP_KEY

    @Provides
    @Singleton
    internal fun provideApolloClient(
        @ApolloServerUrl serverUrl: String,
        headerFactory: HeaderFactory,
        @GraphQLPrefs prefs: SharedPreferences,
        refreshTokenUseCase: RefreshTokenUseCase,
        cloudUrlUtil: CloudUrlUtil,
        @CloudClientKey cloudClientKey: String,
        getAnalyticsHttpInterceptorsUseCase: GetAnalyticsHttpInterceptorsUseCase,
        getAnalyticsTraceInterceptorUseCase: GetAnalyticsTraceInterceptorUseCase,
        @CloudApiSharedPrefs cloudPrefs: SharedPreferences,
        @com.skydio.cloud.data.CloudClientKey clientKey: String,
        @AppPackageName appId: String,
        @CloudClientVersion version: String,
        cloudApiCompatibilityUseCase: CloudApiCompatibilityUseCase,
        cloudAuthStateManager: CloudAuthStateManager,
        @AnalyticsEventListenerFactory eventListenerFactory: EventListener.Factory,
    ): ApolloClient =
    ApolloClient.Builder()
        .serverUrl(serverUrl)
        .okHttpClient(
            OkHttpClient.Builder()
                .apply {
                    if (cloudUrlUtil.hasCustomCert()) {
                        cloudUrlUtil.addCertToOkhttpClient(this)
                    }
                }
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                }
                .apply { getAnalyticsHttpInterceptorsUseCase(cloudUrlUtil.getBaseUrl()).forEach { addInterceptor(it) } }
                .addNetworkInterceptor(getAnalyticsTraceInterceptorUseCase(cloudUrlUtil.getBaseUrl()))
                .addInterceptor(
                    CloudApiAuthzInterceptor(
                        cloudPrefs,
                        clientKey,
                        appId,
                        version,
                        refreshTokenUseCase,
                        cloudApiCompatibilityUseCase,
                        cloudAuthStateManager
                    )
                )
                .addInterceptor(GenericInterceptor(headerFactory, prefs, refreshTokenUseCase, cloudClientKey))
                .eventListenerFactory(eventListenerFactory)
                .build()
        )
        .build()
}
