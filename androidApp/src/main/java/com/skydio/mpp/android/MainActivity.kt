package com.skydio.mpp.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.skydio.mpp.AUTH_TOKEN_KEY
import com.skydio.mpp.DataStoreMaker
import com.skydio.mpp.LocationData
import com.skydio.mpp.LocationTracker
import com.skydio.mpp.login.LoginViewModel
import com.skydio.mpp.ui.LoginView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val hasLocationPermissionFlow = MutableStateFlow(false)
    private val isLoggedIn = mutableStateOf(false)
    private val loginViewModel: LoginViewModel by viewModels()
    private val locationTracker: LocationTracker = LocationTracker()
    private val tokenFlow: Flow<String> = DataStoreMaker.make().data.map { preferences ->
        preferences[AUTH_TOKEN_KEY] ?: ""
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hasLocation = locationTracker.hasLocationPermission()
        val intent = Intent(this, BGService::class.java)
        startService(intent)
        lifecycleScope.launch {
            loginViewModel.checkToken()
        }
        if (hasLocation.not()) {
            requestLocationPermission()
        } else {
            locationTracker.startTracking()
            hasLocationPermissionFlow.update { true }
        }
        setContent {
            MyApplicationTheme(darkTheme = true) {
                if (isLoggedIn.value) {
                    //LocationView(hasLocationPermissionFlow, locationDataFlow)
                } else {
                    LoginView()
                }
            }
        }


        GlobalScope.launch {
            tokenFlow.collect {
                if (it.isNotEmpty()) {
                    isLoggedIn.value = true
                    Log.d("PatrolLink", "Token: $it")
                    startCapacitor(it, null)
                }
            }
        }

    }

    private val requestContactPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasLocationPermissionFlow.update { isGranted }
        }

    private fun requestLocationPermission() {
        requestContactPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        requestContactPermissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    private fun startCapacitor(token: String?, locationData: LocationData?) {
        val intent = Intent()
        token?.let {
            intent.putExtra("token", token)
        }
        locationData?.let {
            intent.putExtra("latitude", locationData.latitude)
            intent.putExtra("longitude", locationData.longitude)
            intent.putExtra("altitude", locationData.altitude)
            intent.putExtra("speed", locationData.speed)
            intent.putExtra("bearing", locationData.bearing)
            intent.putExtra("accuracy", locationData.accuracy)
        }
        intent.setClassName("com.skydio.patrol_link", "com.skydio.patrol_link.MainActivity")
        startActivity(intent)
    }
}
