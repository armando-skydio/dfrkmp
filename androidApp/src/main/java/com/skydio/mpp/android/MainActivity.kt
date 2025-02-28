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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import com.skydio.ui.demo.DemoViewModel

class MainActivity : ComponentActivity()
{
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

