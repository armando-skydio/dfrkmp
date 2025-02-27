package com.skydio.ui.components.widget.selectors

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.*
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget
import com.skydio.ui.components.widget.text.SkydioMarqueeText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a radio group.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioRadioGroup.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    var selectionChangedListener: SelectionChangedListener? = null

    /**
     * The entire state of any given [SkydioRadioGroup].
     */
    data class State(
        override var value: Int = -1,
        val options: List<String> = emptyList(),
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true,
    ) : UserEditableViewState<Int>()

    @Composable
    override fun Content(state: State) = SkydioRadioGroup(
        state = state,
        onSelectionChanged = { selectionChangedListener?.onSelectedIndexChanged(it) }
    )

}

// MARK: Composable Impl

@Composable
fun SkydioRadioGroup(
    state: RadioGroupState,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    onSelectionChanged: (selectedIndex: Int) -> Unit
) = SkydioRadioGroup(
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
fun <T> SkydioRadioGroup(
    selectedIndex: Int,
    options: List<T>,
    getTextFromOptions: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    divider: Boolean = false,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    rowModifier: Modifier = Modifier
        .fillMaxWidth()
        .height(20.dp),
    onSelectionChanged: (T) -> Unit = {}
) = SkydioRadioGroup(
    selectedIndex = selectedIndex,
    options = options.map { getTextFromOptions(it) },
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    textStyle = textStyle,
    rowModifier = rowModifier,
    divider = divider,
    onSelectionChanged = { onSelectionChanged(options[it]) }
)

@Composable
fun SkydioRadioGroup(
    selectedIndex: Int,
    options: List<String>,
    disabledOptions: List<String> = emptyList(),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    rowModifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    divider: Boolean = false,
    colors: RadioGroupColors = RadioGroupColors.defaultThemeColors(theme),
    onSelectionChanged: (selectedIndex: Int) -> Unit = {}
) {
    val textColor = if (enabled) colors.textColor else colors.disabledTextColor
    val radioButtonColors = RadioButtonDefaults.colors(
        selectedColor = colors.buttonColor,
        unselectedColor = colors.buttonColor,
        disabledSelectedColor = colors.disabledButtonColor,
        disabledUnselectedColor = colors.disabledButtonColor)

    var internalSelectedIndex by remember { mutableStateOf(selectedIndex) }
    val externalSelectedIndex by rememberUpdatedState(selectedIndex)
    LaunchedEffect(externalSelectedIndex) { internalSelectedIndex = externalSelectedIndex }

    ProvideTextStyle(value = textStyle.copy(color = textColor)) {
        Column(
            modifier = modifier
        ) {
            options.forEachIndexed { index, text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = rowModifier
                        .clickable {
                            internalSelectedIndex = index; onSelectionChanged(index)
                        }
                ) {
                    RadioButton(
                        enabled = enabled && !disabledOptions.contains(text),
                        selected = internalSelectedIndex == index,
                        colors = radioButtonColors,
                        onClick = {
                            if (!disabledOptions.contains(text)) {
                                internalSelectedIndex = index; onSelectionChanged(index)
                            }
                        })
                    SkydioMarqueeText(text = text, modifier = Modifier.clickable {
                        if (!disabledOptions.contains(text)) {
                            internalSelectedIndex = index; onSelectionChanged(index)
                        }
                    })
                }
                Spacer(modifier = Modifier.height(2.dp))
                if (divider)
                    Divider(
                        thickness = 1.dp,
                        color = theme.colors.disabledTextColor,
                        modifier = Modifier
                            .fillMaxWidth())
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

// MARK: Utils

typealias RadioGroupState = SkydioRadioGroup.State

/**
 * Provides a more readable variable name than [RadioGroupState.value]
 */
val RadioGroupState.selectedIndex: Int get() = value


// MARK: Theming

data class RadioGroupColors(
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
        ) = RadioGroupColors(
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
private fun SkydioRadioGroupPreview() {
    data class ArbitraryStruct(val name: String)

    val options = listOf(
        ArbitraryStruct("Option 1"),
        ArbitraryStruct("Option 2"),
        ArbitraryStruct("Option 3"),
        ArbitraryStruct("Option 4"),
        ArbitraryStruct("Option 5"))
    val selectedIndex = 3
    ThemedPreviews { theme ->
        SkydioRadioGroup(
            selectedIndex = selectedIndex,
            options = options,
            getTextFromOptions = { it.name },
            theme = theme)
    }
}
