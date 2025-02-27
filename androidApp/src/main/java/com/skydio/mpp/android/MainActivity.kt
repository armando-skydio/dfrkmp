package com.skydio.mpp.android

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.linecorp.abc.location.ABCLocation
import com.linecorp.abc.location.extension.processRequestPermissionsResult
import com.skydio.mpp.CommonGPSLocationService
import com.skydio.mpp.Greeting
import com.skydio.mpp.androidIntentFlow
import com.skydio.mpp.appContext
import com.skydio.ui.demo.DemoViewModel
import com.skydio.ui.designsystem.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity()
{
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isSendingUserToAndroidAppSettingsScreen = false
    var isPermissionsGranted = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Collect App Metadata
        val packageName = applicationContext.packageName

        // Setup the permission launcher & callback
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            // Check if permissions were granted
            if(it[Manifest.permission.ACCESS_FINE_LOCATION] == false ||
                it[Manifest.permission.ACCESS_COARSE_LOCATION] == false
            ) {
                isPermissionsGranted = false
                AlertDialog.Builder(this)
                    .setTitle("Location Permissions Required")
                    .setMessage("This app requires location permissions to function. " +
                        "Please enable location permissions in the app settings.")
                    .setPositiveButton("App Settings") { _, _ ->

                        // Intent to open the App Settings
                        Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            @Suppress("RemoveRedundantQualifierName") // We are using the Android flavor of Uri
                            data = android.net.Uri.parse("package:$packageName")
                            startActivity(this)
                        }
                        isSendingUserToAndroidAppSettingsScreen = true
                    }
                    .setNegativeButton("Cancel") { _, _ ->

                        finish()
                    }
                    .show()
            } else {
                isPermissionsGranted = true
            }
        }
        // Get permissions to access location (opens dialog)
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

        // Collects the "command intent" flow from the common module for Android specific code
        // note: for some reason, this doesn't work in the common module, so we must collect the intent
        //       flow from the Android-specific code, and then emit the intent from this MainActivity. __/shrug\__
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            androidIntentFlow.collect { intent ->
                when (intent.action) {
                    CommonGPSLocationService.ACTION_STOP_BACKGROUND_UPDATES -> {
                        stopBackgroundUpdates()
                    }
                    CommonGPSLocationService.ACTION_START_BACKGROUND_UPDATES -> {
                        startBackgroundUpdates()
                    }
                    Intent.ACTION_VIEW -> { // open a web link
                        startActivity(intent)
                    }
                    Intent.ACTION_SEND -> { // send an email
                        try {
                            startActivity(Intent.createChooser(intent, "Send Email"))
                        } catch (e: Exception) {
                        }
                    }
                    "Navigation" -> { // open navigation
                        val lat = intent.getDoubleExtra("lat", 0.0)
                        val lng = intent.getDoubleExtra("lng", 0.0)
                        val markerTitle = intent.getStringExtra("markerTitle") ?: ""
                        startNavigation(lat, lng, markerTitle)
                    }
                }
            }
        }

        // If permissions are not granted yet, show the splash screen for permissions.
        if(!isPermissionsGranted) {
            setContent {

            }
        }

    }

    // Turn off the notification service for the GPS service, which prevents background location updates
    private fun stopBackgroundUpdates() {
        Intent(applicationContext, GPSForegroundNotificationService::class.java).apply {
            action = GPSForegroundNotificationService.ACTION_STOP_NOTIFICATION_SERVICE
            appContext.startService(this) // sends command to stop service
        }
    }

    override fun onDestroy() {

        stopBackgroundUpdates()
        super.onDestroy()
    }

    // Turn on the notification service for the GPS service, which allows background location updates
    private fun startBackgroundUpdates() {
        Intent(applicationContext, GPSForegroundNotificationService::class.java).apply {
            action = GPSForegroundNotificationService.ACTION_START_NOTIFICATION_SERVICE
            appContext.startService(this) // sends command to start service
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(intent?.action == GPSForegroundNotificationService.ACTION_STOP_NOTIFICATION_SERVICE) {
            stopBackgroundUpdates()
        }
    }

    override fun onResume() {
        super.onResume()
        // Relaunch the permission dialog if the user was previously sent to the Android's "App Settings" screen
        if(isSendingUserToAndroidAppSettingsScreen) {
            isSendingUserToAndroidAppSettingsScreen = false
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ))
        }
    }

    private fun startNavigation(
        latitude: Double,
        longitude: Double,
        markerTitle: String,
    ) {
        val uriWaze = Uri.parse("https://waze.com/ul?ll=$latitude,$longitude&navigate=yes")
        val intentWaze = Intent(Intent.ACTION_VIEW, uriWaze)
        intentWaze.setPackage("com.waze")

        val uriGoogle = "google.navigation:q=$latitude,$longitude"
        val intentGoogleNav = Intent(Intent.ACTION_VIEW, Uri.parse(uriGoogle))
        intentGoogleNav.setPackage("com.google.android.apps.maps")

        val title = "Choose nav for marker:\n$markerTitle"
        val chooserIntent = Intent.createChooser(intentGoogleNav, title)
        val arr = arrayOfNulls<Intent>(1)
        arr[0] = intentWaze
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arr)

        try {
            startActivity(chooserIntent)
        } catch (e: ActivityNotFoundException) {
            // If Waze is not installed, open it in Google Play:
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"))
            startActivity(intent)
        }
    }

}

@Preview
@Composable
fun Test() {
    Text("Hello World") // previews work here
}

