package com.skydio.ui.components.widget.selectors

/**
 * Functional callback interface for selected index changed events.
 * See [SkydioRadioGroup] or [SkydioTextSegmentControl]
 */
fun interface SelectionChangedListener {
    fun onSelectedIndexChanged(selectedIndex: Int)
}
