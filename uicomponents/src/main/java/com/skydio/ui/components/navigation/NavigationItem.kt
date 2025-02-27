package com.skydio.ui.components.navigation

import com.skydio.ui.util.ImageSource

data class NavigationItem(
    val uid: String,
    val label: String,
    val icon: ImageSource,
    val isSelected: Boolean = false,
    val isEnabled: Boolean = true,
    val data: Any? = null
)
