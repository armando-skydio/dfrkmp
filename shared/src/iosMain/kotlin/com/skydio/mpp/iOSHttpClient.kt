package com.skydio.mpp

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.DarwinClientEngineConfig

actual fun HttpClientConfig<*>.configureForPlatform() {
    engine {
        this as DarwinClientEngineConfig
        // TODO: Add iOS config
    }
}