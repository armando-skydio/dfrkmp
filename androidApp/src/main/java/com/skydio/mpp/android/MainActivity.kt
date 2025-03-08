package com.skydio.mpp.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skydio.mpp.LocationData
import com.skydio.mpp.LocationTracker
import com.skydio.mpp.login.LoginViewModel
import com.skydio.mpp.ui.LocationView
import com.skydio.mpp.ui.LoginView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val hasLocationPermissionFlow = MutableStateFlow(false)
    private val locationDataFlow = MutableStateFlow<LocationData?>(null)
    private val isLoggedIn = mutableStateOf(false)
    //private val loginViewModel: LoginViewModel by viewModel()

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
                if (isLoggedIn.value) {
                    LocationView(hasLocationPermissionFlow, locationDataFlow)
                } else {
                    LoginView()
                }
            }
        }

        lifecycleScope.launch {
            /*loginViewModel.tokenFlow.collectLatest {
                if (it.isNotEmpty()) {
                    isLoggedIn.value = true
                    Log.d("PatrolLink", "Token: $it")
                }
            }*/
        }

        lifecycleScope.launch {
            locationDataFlow.collectLatest {
                it?.let {
                    Log.d("PatrolLink", "Location: $it")
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
    }
}


@Preview
@Composable
fun Test() {
    Text("Hello World") // previews work here
}
