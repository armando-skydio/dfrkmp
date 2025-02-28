package com.skydio.mpp

actual class LocationTracker actual constructor() {
    actual fun startTracking() {
    }

    actual fun stopTracking() {
    }

    actual fun addLocationListener(listener: SkyLocationListener) {
    }

    actual fun removeLocationListener(listener: SkyLocationListener) {
    }

    actual val tracking: Boolean
        get() = false
}
