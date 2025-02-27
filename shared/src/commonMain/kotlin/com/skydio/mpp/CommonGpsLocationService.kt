package com.skydio.mpp

import kotlinx.serialization.Serializable


@Serializable
data class LatLong(val latitude: Double = 0.0, val longitude: Double = 0.0)

// Define a data class to store latitude and longitude coordinates
@Serializable
data class Location(val latitude: Double, val longitude: Double)

fun Location.isLocationValid(): Boolean {
    return latitude != 0.0 && longitude != 0.0
}

fun LatLong.toLocation(): Location {
    return Location(latitude, longitude)
}

data class Heading(val trueHeading: Double, val magneticHeading: Double)
// Define a common class for location service
expect class CommonGPSLocationService() {
    suspend fun getCurrentGPSLocationOneTime(): Location
    suspend fun onUpdatedGPSLocation(
        errorCallback: (String) -> Unit = {},
        locationCallback: (Location?) -> Unit
    )
    suspend fun currentHeading(callback: (Heading?) -> Unit)
    fun getLatestGPSLocation(): Location?

    fun allowBackgroundLocationUpdates()
    fun preventBackgroundLocationUpdates()
}