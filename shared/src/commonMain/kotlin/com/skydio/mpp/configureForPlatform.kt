package com.skydio.mpp

import io.ktor.client.HttpClientConfig

expect fun HttpClientConfig<*>.configureForPlatform()