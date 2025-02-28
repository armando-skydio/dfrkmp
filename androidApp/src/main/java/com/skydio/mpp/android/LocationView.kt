package com.skydio.mpp.android

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydio.mpp.LocationData
import com.skydio.mpp.LocationTracker
import com.skydio.mpp.SkyLocationListener
import com.skydio.ui.components.widget.text.SkydioText


fun hasLocationPermission(context: Context): Boolean {
    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    val res: Int = context.checkCallingOrSelfPermission(permission)
    return (res == PackageManager.PERMISSION_GRANTED)
}

@Composable
fun LocationView() {


    var hasLocationPermission = hasLocationPermission(LocalContext.current)

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        hasLocationPermission = true
    }

    val locationTracker by remember { mutableStateOf(LocationTracker()) }
    var location by remember { mutableStateOf<LocationData?>(null) }
    var tracking by remember { mutableStateOf(false) }


    val locationListener = object : SkyLocationListener {
        override fun onNewLocation(data: LocationData) {
            location = data
            Log.e("chengz", "$data")
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


    LaunchedEffect(key1 = "permission") {
        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    DisposableEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            changeTrackingState(true)
        }
        onDispose {
            if (hasLocationPermission) {
                changeTrackingState(false)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SkydioText(
                text = location.toString(),
                style = TextStyle.Default.copy(fontSize = 16.sp),
                color = Color.White,
            )

            Spacer(modifier = Modifier.size(56.dp))

            Button(
                onClick = {
                    changeTrackingState(locationTracker.tracking.not())
                }
            ) {
                SkydioText(
                    text = if (tracking) "Stop tracking" else "Start tracking",
                    style = TextStyle.Default.copy(fontSize = 14.sp),
                    color = Color.White,
                )
            }
        }
    }


}
