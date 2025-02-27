package com.skydio.ui.components.navigation

import androidx.annotation.DrawableRes

enum class NavigationAction(@DrawableRes val icon: Int) {
    BACK(com.skydio.ui.components.R.drawable.ic_back),
    CLOSE(com.skydio.ui.components.R.drawable.ic_close),
    SETTINGS(com.skydio.ui.components.R.drawable.ic_settings)
}
