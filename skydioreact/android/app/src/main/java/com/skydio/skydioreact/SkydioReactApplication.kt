package com.skydio.skydioreact

import android.app.Application
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint
import com.facebook.react.defaults.DefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader

class SkydioReactApplication : Application(), ReactApplication {

        override val reactNativeHost: ReactNativeHost =
            object : DefaultReactNativeHost(this) {
                override fun getPackages(): List<ReactPackage> = PackageList(this).packages
                override fun getJSMainModuleName(): String = "index"
                override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG
                override val isNewArchEnabled: Boolean = true
                override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
            }

        override val reactHost: ReactHost
            get() = DefaultReactHost.getDefaultReactHost(applicationContext, reactNativeHost)

        override fun onCreate() {
            super.onCreate()
            SoLoader.init(this, true)
            if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
                DefaultNewArchitectureEntryPoint.load();
                //load()
            }
        }
    }