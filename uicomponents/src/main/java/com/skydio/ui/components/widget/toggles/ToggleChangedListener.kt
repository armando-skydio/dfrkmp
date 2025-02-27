package com.skydio.ui.components.widget.toggles

/**
 * Functional callback interface for toggle state changes.
 */
fun interface ToggleChangedListener {
    fun onToggleChanged(isChecked: Boolean)
}
