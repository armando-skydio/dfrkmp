package com.skydio.mpp.android

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import com.skydio.ui.demo.DemoViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: DemoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ABCLocation
            .onPermissionUpdated(this) { isGranted ->
                println("onPermissionUpdated -> isGranted: $isGranted")
            }
            .onLocationUnavailable(this) {
                println("onLocationUnavailable")
            }

        setContent {
            MyApplicationTheme(darkTheme = true) {
                LoginView()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        ABCLocation.stopLocationUpdating()
    }

    private fun showDialog(message: String) {
        val myAlertBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        myAlertBuilder.setTitle("")
        myAlertBuilder.setMessage(message)
        myAlertBuilder.setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ -> })
        myAlertBuilder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ABCLocation.processRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
