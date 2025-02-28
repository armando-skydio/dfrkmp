package com.skydio.mpp

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLDeviceOrientationPortrait
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLDistanceFilterNone
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.CoreLocation.kCLLocationAccuracyBestForNavigation
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.concurrent.AtomicReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// Implement the LocationService in iOS
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CommonGPSLocationService  {

    // Define a native CLLocationManager object
    private val locationManager = CLLocationManager()
    private val oneTimeLocationManager = CLLocationManager()
    private val locationDelegate = LocationDelegate()

    // Define an atomic reference to store the latest location
    private val latestLocation = AtomicReference<Location?>(null)

    // Define a custom delegate that extends NSObject and implements CLLocationManagerDelegateProtocol
    private class LocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {

        // Define a callback to receive location updates
        var onLocationUpdate: ((Location?) -> Unit)? = null

        @OptIn(ExperimentalForeignApi::class)
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            didUpdateLocations.firstOrNull()?.let {
                val location = it as CLLocation
                location.coordinate.useContents {
                    onLocationUpdate?.invoke(Location(latitude, longitude))
                }

            }
        }

        // Define a callback to receive heading updates
        var onHeadingUpdate: ((Heading?) -> Unit)? = null

        override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
            onHeadingUpdate?.invoke(Heading(didUpdateHeading.trueHeading, didUpdateHeading.magneticHeading))
        }

        override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
            onLocationUpdate?.invoke(null)
        }

        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: Int) {

        }

        override fun locationManagerDidPauseLocationUpdates(manager: CLLocationManager) {
        }

        override fun locationManagerDidResumeLocationUpdates(manager: CLLocationManager) {
        }

    }

    actual suspend fun onUpdatedGPSLocation(
        errorCallback: (String) -> Unit,  // not used in iOS
        locationCallback: (Location?) -> Unit
    ) {
        locationManager.requestWhenInUseAuthorization()  // for background location updates
        locationDelegate.onLocationUpdate = locationCallback
        locationManager.delegate = locationDelegate

        locationManager.pausesLocationUpdatesAutomatically = true  // to save battery
        locationManager.showsBackgroundLocationIndicator = true // required for background location updates
        locationManager.allowsBackgroundLocationUpdates = true // required for background location updates
        locationManager.distanceFilter = kCLDistanceFilterNone
        locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
        locationManager.startUpdatingLocation()
    }

    actual suspend fun currentHeading(callback: (Heading?) -> Unit) {
        locationManager.requestWhenInUseAuthorization()


        locationDelegate.onHeadingUpdate = callback
        locationManager.delegate = locationDelegate

        locationManager.headingOrientation = CLDeviceOrientationPortrait
        locationManager.startUpdatingHeading()
    }

    // Get the current location only one time (not a stream)
    actual suspend fun getCurrentGPSLocationOneTime(): Location = suspendCoroutine { continuation ->
        oneTimeLocationManager.requestWhenInUseAuthorization()
        oneTimeLocationManager.desiredAccuracy = kCLLocationAccuracyBest

        oneTimeLocationManager.startUpdatingLocation()

        // Define a callback to receive location updates
        val locationDelegate = LocationDelegate()
        locationDelegate.onLocationUpdate = { location ->
            oneTimeLocationManager.stopUpdatingLocation()
            latestLocation.getAndSet(location)

            location?.run {
                continuation.resume(this)
            } ?: run {
                continuation.resumeWithException(Exception("Unable to get current location - 2"))
            }
        }
        oneTimeLocationManager.delegate = locationDelegate
    }

    actual fun getLatestGPSLocation(): Location? {
        return latestLocation.value
    }

    actual fun allowBackgroundLocationUpdates() {
        locationManager.allowsBackgroundLocationUpdates = true
    }

    actual fun preventBackgroundLocationUpdates() {
        locationManager.allowsBackgroundLocationUpdates = false
    }
}