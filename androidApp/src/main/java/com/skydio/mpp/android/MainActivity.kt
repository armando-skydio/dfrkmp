package com.skydio.mpp.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.skydio.mpp.login.LoginViewModel
import com.skydio.mpp.ui.LoginView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity()
{

    private val loginViewModel by viewModels<LoginViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme(darkTheme = true) {
                LoginView()
            }
        }

        lifecycleScope.launch {
            loginViewModel.getToken().collect { token ->
                if (token.isNotEmpty()) {
                    startCapacitor(token)
                }
            }
        }
    }

    fun startCapacitor(token:String) {
        val intent = Intent()
        intent.putExtra("token", token)
        intent.setClassName("com.skydio.patrol_link", "com.skydio.patrol_link.MainActivity")
        startActivityForResult(intent, 0)
    }
}

