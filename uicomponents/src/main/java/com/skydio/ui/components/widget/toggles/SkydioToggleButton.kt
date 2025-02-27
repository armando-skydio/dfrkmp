package com.skydio.ui.components.widget.toggles

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.*
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.util.extension.clickableIf
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Blue500
import com.skydio.ui.designsystem.Gray0
import com.skydio.ui.designsystem.getAppTheme

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a toggle button.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioToggleButton.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    var onToggleListener: ToggleChangedListener? = null

    /**
     * The entire state of any given [SkydioToggleButton].
     */
    data class State(
        val label: String = "",
        override var value: Boolean = false,
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true,
    ) : UserEditableViewState<Boolean>()

    @Composable
    override fun Content(state: State) = SkydioToggleButton(
        state = state,
        onToggleChanged = { onToggleListener?.onToggleChanged(it) }
    )

}

// MARK: Composable Impl

@Composable
fun SkydioToggleButton(
    state: ToggleButtonState,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    colors: ToggleButtonColors = ToggleButtonColors.defaultThemeColors(theme),
    onToggleChanged: (Boolean) -> Unit = {},
) = SkydioToggleButton(
    isToggledOn = state.isToggledOn,
    label = state.label,
    isEnabled = state.isEnabled,
    modifier = modifier,
    theme = theme,
    textStyle = textStyle,
    shape = shape,
    colors = colors,
    onToggleChanged = {
        state.value = it
        onToggleChanged(it)
    },
)

@Composable
fun SkydioToggleButton(
    modifier: Modifier = Modifier,
    // state
    label: String = "",
    isToggledOn: Boolean = false,
    isEnabled: Boolean = true,
    // styling
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    colors: ToggleButtonColors = ToggleButtonColors.defaultThemeColors(theme),
    onToggleChanged: (Boolean) -> Unit = {},
) {
    val bgColor = when {
        isToggledOn -> colors.toggledOnBackgroundColor
        else -> Color.Transparent
    }
    val outlineColor = when {
        isToggledOn -> Color.Transparent
        isEnabled -> colors.toggledOffOutlineColor
        else -> colors.disabledOutlineColor
    }
    val textColor = when {
        isToggledOn -> colors.toggledOnTextColor
        isEnabled -> colors.toggledOffTextColor
        else -> colors.disabledTextColor
    }
    val (isToggled, setToggled) = remember { mutableStateOf(isToggledOn) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(32.dp)
            .background(color = bgColor, shape = shape)
            .border(width = 1.dp, color = outlineColor, shape = shape)
            .clickableIf(isEnabled) { setToggled(!isToggled); onToggleChanged(isToggled) },
    ) {
        Text(
            text = label,
            style = textStyle,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp))
    }
}

// MARK: Utils

typealias ToggleButtonState = SkydioToggleButton.State

/**
 * Provides a more readable variable name than [ToggleButtonState.value]
 */
val ToggleButtonState.isToggledOn: Boolean get() = value

// MARK: Theming

data class ToggleButtonColors(
    val toggledOnTextColor: Color,
    val toggledOffTextColor: Color,
    val disabledTextColor: Color,
    val toggledOnBackgroundColor: Color,
    val toggledOffOutlineColor: Color,
    val disabledOutlineColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            toggledOnTextColor: Color = Gray0,
            toggledOffTextColor: Color = theme.colors.primaryTextColor,
            disabledTextColor: Color = theme.colors.disabledTextColor,
            toggledOnBackgroundColor: Color = Blue500,
            toggledOffOutlineColor: Color = theme.colors.disabledTextColor,
            disabledOutlineColor: Color = theme.colors.disabledTextColor,
        ) = ToggleButtonColors(
            toggledOnTextColor = toggledOnTextColor,
            toggledOffTextColor = toggledOffTextColor,
            disabledTextColor = disabledTextColor,
            toggledOnBackgroundColor = toggledOnBackgroundColor,
            toggledOffOutlineColor = toggledOffOutlineColor,
            disabledOutlineColor = disabledOutlineColor,
        )
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun SkydioToggleButtonPreview() {
    ThemedPreviews { theme ->
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SkydioToggleButton(label = "On", isEnabled = true, isToggledOn = true, theme = theme)
            SkydioToggleButton(label = "Off", isEnabled = true, isToggledOn = false, theme = theme)
            SkydioToggleButton(label = "Disabled", isEnabled = false, theme = theme)
        }
    }
}
