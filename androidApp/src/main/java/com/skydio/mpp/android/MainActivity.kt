package com.skydio.mpp.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.skydio.mpp.LocationTracker
import com.skydio.mpp.ui.LocationView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isSendingUserToAndroidAppSettingsScreen = false
    var isPermissionsGranted = false

    val hasLocationPermissionFlow = MutableStateFlow(false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tracker = LocationTracker()
        val hasLocation = tracker.hasLocationPermission()
        if (hasLocation.not()) {
            requestLocationPermission()
        } else {
            hasLocationPermissionFlow.update { true }
        }
        setContent {
            MyApplicationTheme(darkTheme = true) {
                //LoginView()
                LocationView(hasLocationPermissionFlow)
            }
        }
    }

    private val requestContactPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasLocationPermissionFlow.update { isGranted }
        }

    private fun requestLocationPermission() {
        requestContactPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}


@Preview
@Composable
fun Test() {
    Text("Hello World") // previews work here
}
