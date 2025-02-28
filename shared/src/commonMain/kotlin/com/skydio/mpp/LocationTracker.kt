package com.skydio.mpp

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val bearing: Float,
    val speed: Float,
    val accuracy: Float,
)

expect class LocationTracker() {
    fun startTracking()
    fun stopTracking()
    fun addLocationListener(listener: SkyLocationListener)
    fun removeLocationListener(listener: SkyLocationListener)
    val tracking: Boolean
}

interface SkyLocationListener {
    fun onNewLocation(data: LocationData)
}
