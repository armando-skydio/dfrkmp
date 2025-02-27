package com.skydio.ui.util.extension

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp

/**
 * Modifier to enable clicking, without the default ripple effect.
 */
fun Modifier.noRippleClickable(
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit = {},
) = composed {
    clickable(
        interactionSource = interactionSource ?: remember { MutableInteractionSource() },
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
    )
}

/**
 * Modifier to enable clicking, without the default ripple effect, but only if [condition] is true.
 */
fun Modifier.noRippleClickableIf(
    condition: Boolean,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit = {},
) = if (condition) noRippleClickable(
    interactionSource,
    indication,
    enabled,
    onClickLabel,
    role,
    onClick
) else this

/**
 * Modifier to enable clicking, but only if the [condition] is true.
 */
fun Modifier.clickableIf(condition: Boolean, onClick: () -> Unit): Modifier {
    return if (condition) clickable { onClick() }
    else this
}

fun Modifier.ignoreHorizontalParentPadding(horizontal: Dp): Modifier {
    return this.layout { measurable, constraints ->
        val overridenWidth = constraints.maxWidth + 2 * horizontal.roundToPx()
        val placeable = measurable.measure(constraints.copy(maxWidth = overridenWidth))
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}
