package com.skydio.mpp.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.skydio.mpp.appContext

class App: Application() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()

        // Create the notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Marker Status Notifications",
                NotificationManager.IMPORTANCE_LOW // IMPORTANCE_HIGH will make the notification sound
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        appContext = applicationContext
    }
}
