package com.skydio.mpp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager

actual class LocationTracker actual constructor() {

    private val listeners = mutableListOf<SkyLocationListener>()

    private val locationManager: LocationManager =
        AppContext.get().getSystemService(LOCATION_SERVICE) as LocationManager

    private var _tracking = false

    @SuppressLint("MissingPermission")
    actual fun startTracking() {
        _tracking = true
        locationManager.requestLocationUpdates(
            LocationManager.FUSED_PROVIDER,
            100L,
            1F,
            listener
        )
    }

    actual fun stopTracking() {
        _tracking = false
        locationManager.removeUpdates(listener)
    }

    actual fun addLocationListener(listener: SkyLocationListener) {
        listeners.add(listener)
    }

    private val listener = LocationListener {
        val data = LocationData(
            latitude = it.latitude,
            longitude = it.longitude,
            altitude = it.altitude,
            bearing = it.bearing,
            speed = it.speed,
            accuracy = it.accuracy,
        )
        for (listener in listeners) {
            listener.onNewLocation(data)
        }
    }

    actual fun removeLocationListener(listener: SkyLocationListener) {
        listeners.remove(listener)
    }

    actual val tracking: Boolean
        get() = _tracking

    actual fun hasLocationPermission(): Boolean {
        val context = AppContext.get()
        val permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION
        val res: Int = context.checkCallingOrSelfPermission(permission)
        return (res == PackageManager.PERMISSION_GRANTED)
    }

}
