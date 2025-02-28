package com.skydio.mpp.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.skydio.mpp.ui.LoginView

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isSendingUserToAndroidAppSettingsScreen = false
    var isPermissionsGranted = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme(darkTheme = true) {
                LoginView()
                //LocationView()
            }
        }
    }

}

@Preview
@Composable
fun Test() {
    Text("Hello World") // previews work here
}
