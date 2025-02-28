package com.skydio.mpp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import androidx.compose.ui.unit.sp
import com.skydio.mpp.LocationData
import com.skydio.mpp.LocationTracker
import com.skydio.mpp.SkyLocationListener
import kotlinx.coroutines.flow.Flow

@Composable
fun LocationView(
    locationPermissionFlow: Flow<Boolean>,
    onLocationChangeFlow: MutableStateFlow<LocationData?>
) {

    val hasLocationPermission = locationPermissionFlow.collectAsState(false)

    val locationTracker by remember { mutableStateOf(LocationTracker()) }
    var location by remember { mutableStateOf<LocationData?>(null) }
    var tracking by remember { mutableStateOf(false) }

    val locationListener = object : SkyLocationListener {
        override fun onNewLocation(data: LocationData) {
            location = data
            onLocationChangeFlow.update { data }
        }
    }

    fun changeTrackingState(track: Boolean) {
        if (track) {
            locationTracker.startTracking()
            locationTracker.addLocationListener(locationListener)
            tracking = true
        } else {
            locationTracker.removeLocationListener(locationListener)
            locationTracker.stopTracking()
            tracking = false
        }
    }

    DisposableEffect(hasLocationPermission.value) {
        if (hasLocationPermission.value) {
            changeTrackingState(true)
        }
        onDispose {
            if (hasLocationPermission.value) {
                changeTrackingState(false)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 48.dp, horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = location.toString(),
                style = TextStyle.Default.copy(fontSize = 16.sp),
                color = Color.Black,
            )

            Spacer(modifier = Modifier.size(56.dp))

            Button(
                onClick = {
                    changeTrackingState(locationTracker.tracking.not())
                }
            ) {
                Text(
                    text = if (tracking) "Stop tracking" else "Start tracking",
                    style = TextStyle.Default.copy(fontSize = 14.sp),
                    color = Color.White,
                )
            }
        }
    }


}
