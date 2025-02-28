package com.skydio.mpp.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydio.mpp.LocationData
import com.skydio.mpp.LocationTracker
import com.skydio.mpp.SkyLocationListener

/*fun hasLocationPermission(context: Context): Boolean {
    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    val res: Int = context.checkCallingOrSelfPermission(permission)
    return (res == PackageManager.PERMISSION_GRANTED)
}*/

@Composable
fun LocationView() {


    //var hasLocationPermission = hasLocationPermission(LocalContext.current)
    var hasLocationPermission = false

    /*val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        hasLocationPermission = true
    }*/

    val locationTracker by remember { mutableStateOf(LocationTracker()) }
    var location by remember { mutableStateOf<LocationData?>(null) }
    var tracking by remember { mutableStateOf(false) }


    val locationListener = object : SkyLocationListener {
        override fun onNewLocation(data: LocationData) {
            location = data
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
        //permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
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

            Text(
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
                Text(
                    text = if (tracking) "Stop tracking" else "Start tracking",
                    style = TextStyle.Default.copy(fontSize = 14.sp),
                    color = Color.White,
                )
            }
        }
    }


}
