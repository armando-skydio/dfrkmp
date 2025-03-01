package com.skydio.mpp.android

import android.app.Application
import com.skydio.mpp.AppContext
import com.skydio.mpp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class KmpApp: Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        AppContext.setUp(applicationContext)
        initKoin {
            androidContext(this@KmpApp)
        }
    }

}
