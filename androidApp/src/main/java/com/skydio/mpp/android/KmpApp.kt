package com.skydio.mpp.android

import android.app.Application
import com.skydio.mpp.AppContext

class KmpApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.setUp(applicationContext)
    }

}
