package com.skydio.ui.components.widget.selectors

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.SkydioPopup
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Gray700
import com.skydio.ui.designsystem.Gray800
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.util.extension.noRippleClickable

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a radio group.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioDropdownSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioDropdownSelector.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    var selectionChangedListener: SelectionChangedListener? = null

    /**
     * The entire state of any given [SkydioDropdownSelector].
     */
    data class State(
        var label: String = "",
        override var value: Int = -1,
        val options: List<String> = emptyList(),
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true,
    ) : UserEditableViewState<Int>()

    @Composable
    override fun Content(state: State) = SkydioDropdownSelector(
        state = state,
        onSelectionChanged = { selectionChangedListener?.onSelectedIndexChanged(it) }
    )

}

// MARK: Composable Impl

@Composable
fun SkydioDropdownSelector(
    state: DropdownState,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    onSelectionChanged: (selectedIndex: Int) -> Unit
) = SkydioDropdownSelector(
    label = state.label,
    selectedIndex = state.value,
    options = state.options,
    enabled = state.isEnabled,
    modifier = modifier,
    theme = theme,
    onSelectionChanged = {
        state.value = it
        onSelectionChanged(it)
    }
)

@Composable
fun <T> SkydioDropdownSelector(
    selectedIndex: Int,
    options: List<T>,
    getTextFromOptions: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    onSelectionChanged: (T) -> Unit = {},
    onClick: () -> Unit = {},
) = SkydioDropdownSelector(
    label = label,
    selectedIndex = selectedIndex,
    options = options.map { getTextFromOptions(it) },
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    onSelectionChanged = { onSelectionChanged(options[it]) },
    onClick = onClick
)

@Suppress("NAME_SHADOWING")
@Composable
fun <T> SkydioDropdownSelector(
    items: List<T>,
    itemToString: (T) -> String,
    modifier: Modifier = Modifier,
    label: String? = null,
    selectedIndex: Int = 0,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    colors: SkydioDropdownColors = SkydioDropdownColors.defaultThemeColors(theme),
    onSelectionChanged: (selectedIndex: Int) -> Unit = {},
    onClick: () -> Unit = {},
) {
    val items by rememberUpdatedState(items)
    val selectedIndex by rememberUpdatedState(selectedIndex)
    val strings by remember { derivedStateOf { items.map(itemToString) } }

    SkydioDropdownSelector(
        label = label,
        options = strings,
        modifier = modifier,
        selectedIndex = selectedIndex,
        onSelectionChanged = onSelectionChanged,
        onClick = onClick,
        theme = theme,
        textStyle = textStyle,
        enabled = enabled,
        colors = colors
    )
}

@Composable
private fun SkydioDropdownSelector(
    selectedIndex: Int,
    options: List<String>,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.body,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    colors: SkydioDropdownColors = SkydioDropdownColors.defaultThemeColors(theme),
    onSelectionChanged: (selectedIndex: Int) -> Unit = {},
    onClick: () -> Unit = {},
) {
    val density = LocalDensity.current
    val textColor = if (enabled) colors.textColor else colors.disabledTextColor

    val externalSelectedIndex by rememberUpdatedState(selectedIndex)
    var internalSelectedIndex by remember { mutableIntStateOf(externalSelectedIndex) }
    LaunchedEffect(externalSelectedIndex) { internalSelectedIndex = externalSelectedIndex }


    var expanded by remember { mutableStateOf(false) }
    var widgetSize by remember { mutableStateOf(IntSize.Zero) }
    val widgetSizeDp by remember {
        derivedStateOf {
            with(density) {
                DpSize(
                    width = widgetSize.width.toDp(),
                    height = widgetSize.height.toDp())
            }
        }
    }

    ProvideTextStyle(value = textStyle.copy(color = textColor)) {
        Box {
            Box(
                modifier = modifier
                    .height(32.dp)
                    .background(
                        color = if (enabled) theme.colors.defaultWidgetBackgroundColor else Gray800,
                        shape = shape)
                    .border(
                        width = 1.dp,
                        color = if (enabled) theme.colors.defaultWidgetOutlineColor else Gray700,
                        shape = shape)
                    .clickable {
                        if (enabled) {
                            expanded = !expanded
                        } else {
                            onClick()
                        }
                    }
                    .onSizeChanged { widgetSize = it }
                    .padding(horizontal = 8.dp)
            ) {
                ProvideTextStyle(value = theme.typography.bodyLarge) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProvideTextStyle(value = textStyle.copy(color = theme.colors.primaryTextColor)) {
                            label?.let { Text(label) }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        if (selectedIndex >= 0 && selectedIndex < options.size)
                            Text(text =options[selectedIndex], color = if (enabled) theme.colors.primaryTextColor else theme.colors.disabledTextColor)

                        Spacer(Modifier.weight(1f))

                        if (enabled) {
                            Icon(
                                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = if (expanded) {
                                    stringResource(R.string.show_less)
                                } else {
                                    stringResource(R.string.show_more)
                                },
                                tint = theme.colors.primaryTextColor
                            )
                        }
                    }
                }

                // consume touches when expanded to prevent double opens
                if (expanded) Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable())
            }

            if (expanded) SkydioPopup(
                onDismissRequest = { expanded = false },
                alignment = Alignment.TopCenter,
                offset = IntOffset(y = widgetSize.height, x = 0)
            ) {

                Column(
                    modifier = Modifier
                        .width(widgetSizeDp.width)
                        .padding(top = 4.dp)
                        .background(
                            color = theme.colors.defaultWidgetBackgroundColor,
                            shape = shape)
                        .align(Alignment.BottomEnd)
                        .verticalScroll(rememberScrollState())
                ) {
                    options.forEachIndexed { index, itemValue ->
                        val bgColor =
                            if (index == internalSelectedIndex) theme.colors.selectedItemBackground
                            else theme.colors.defaultWidgetBackgroundColor
                        val borderColor =
                            if (index == internalSelectedIndex) theme.colors.selectedItemBackground
                            else theme.colors.defaultWidgetBackgroundColor
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(widgetSizeDp)
                                .background(color = bgColor, shape = shape)
                                .border(width = 1.dp, color = borderColor, shape = shape)
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    expanded = false
                                    internalSelectedIndex = index
                                    onSelectionChanged(index)
                                }
                        ) {
                            SkydioText(
                                text = itemValue,
                                color = textColor,
                                style = textStyle,
                                modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}


// MARK: Utils

typealias DropdownState = SkydioDropdownSelector.State

/**
 * Provides a more readable variable name than [RadioGroupState.value]
 */
val DropdownState.selectedIndex: Int get() = value

// MARK: Theming

data class SkydioDropdownColors(
    val textColor: Color,
    val disabledTextColor: Color,
    val buttonColor: Color,
    val disabledButtonColor: Color
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            textColor: Color = theme.colors.primaryTextColor,
            disabledTextColor: Color = theme.colors.disabledTextColor,
            buttonColor: Color = theme.colors.primaryTextColor,
            disabledButtonColor: Color = theme.colors.disabledTextColor,
        ) = SkydioDropdownColors(
            textColor = textColor,
            disabledTextColor = disabledTextColor,
            buttonColor = buttonColor,
            disabledButtonColor = disabledButtonColor,
        )
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun SkydioDropdownPreview() {
    data class ArbitraryStruct(val name: String)

    val options = listOf(
        ArbitraryStruct("Option 1"),
        ArbitraryStruct("Option 2"),
        ArbitraryStruct("Option 3"),
        ArbitraryStruct("Option 4"),
        ArbitraryStruct("Option 5"))
    val selectedIndex = 3
    ThemedPreviews { theme ->
        SkydioDropdownSelector(
            label = "Test label",
            selectedIndex = selectedIndex,
            options = options,
            getTextFromOptions = { it.name },
            theme = theme)
    }
}
