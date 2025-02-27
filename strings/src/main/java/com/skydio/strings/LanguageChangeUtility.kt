package com.skydio.strings

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import java.util.Locale

internal fun updateSystemLanguage(
    activity: Activity,
    locale: Locale,
    flags: Int,
    extras: Bundle? = null,
) {
    activity.apply {
        // Update system language
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, this.resources.displayMetrics)

        // Restart activity to show localised strings
        val intent = Intent(activity, activity::class.java)
        intent.addFlags(flags or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        extras?.let { intent.putExtras(extras) }
        startActivity(intent)
    }
}
