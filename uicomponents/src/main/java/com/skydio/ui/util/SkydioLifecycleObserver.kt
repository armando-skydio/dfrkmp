package com.skydio.ui.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference

object SkydioLifecycleObserver : ActivityLifecycleHandler() {

    private var topActivityRef: WeakReference<Activity?> = WeakReference(null)

    val topActivity: Activity?
        get() = topActivityRef.get()

    fun start(context: Context) {
        val app = context.applicationContext as Application
        app.registerActivityLifecycleCallbacks(this)
        if (context is Activity) {
            topActivityRef = WeakReference(context)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        topActivityRef = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        topActivityRef.clear()
    }

}

abstract class ActivityLifecycleHandler : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) { /* no-op */ }
    override fun onActivityStarted(activity: Activity) { /* no-op */ }
    override fun onActivityStopped(activity: Activity) { /* no-op */ }
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { /* no-op */ }
    override fun onActivityDestroyed(activity: Activity) { /* no-op */ }
}
