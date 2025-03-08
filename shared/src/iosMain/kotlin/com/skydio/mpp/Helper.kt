package com.skydio.mpp

import com.skydio.mpp.di.sharedModule
import org.koin.core.context.startKoin
import platform.UIKit.UIDevice

fun doInitKoin(){
    startKoin {
        modules(
            sharedModule
        )
    }
}