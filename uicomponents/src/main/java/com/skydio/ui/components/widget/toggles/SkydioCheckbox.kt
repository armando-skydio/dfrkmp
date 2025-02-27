package com.skydio.ui.components.widget.toggles

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.*
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a checkbox.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioCheckbox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioCheckbox.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    var onCheckChangedListener: ToggleChangedListener? = null

    /**
     * The entire state of any given [SkydioCheckbox].
     */
    data class State(
        override var value: Boolean = false,
        val label: String? = null,
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true,
    ) : UserEditableViewState<Boolean>()

    @Composable
    override fun Content(state: State) = SkydioCheckbox(
        state = state,
        onCheckChanged = { onCheckChangedListener?.onToggleChanged(it) }
    )

}

// MARK: Composable Impl

@Composable
fun SkydioCheckbox(
    state: CheckboxState,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    onCheckChanged: (isChecked: Boolean) -> Unit
) = SkydioCheckbox(
    isChecked = state.value,
    label = state.label,
    enabled = state.isEnabled,
    modifier = modifier,
    theme = theme,
    onCheckChanged = {
        state.value = it
        onCheckChanged(it)
    }
)

@Composable
fun SkydioCheckbox(
    isChecked: Boolean,
    label: String?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    colors: CheckboxColors = CheckboxColors.defaultThemeColors(theme),
    textStyle: TextStyle = theme.typography.bodyLarge,
    height: Dp = 48.dp,
    boxSize: Dp = 24.dp,
    onCheckChanged: (isChecked: Boolean) -> Unit
) {

    val checkedState = remember { mutableStateOf(isChecked) }
    fun updateState(isChecked: Boolean) {
        if (!enabled) return
        checkedState.value = isChecked
        onCheckChanged(isChecked)
    }

    val backgroundColor =
        if (checkedState.value && enabled) colors.checkedBackgroundColor
        else if (checkedState.value && !enabled) colors.disabledCheckedBackgroundColor
        else colors.uncheckedBackgroundColor
    val outlineColor =
        if (checkedState.value) colors.checkedBorderColor
        else colors.uncheckedBorderColor

    Row(modifier = Modifier.height(height)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clip(theme.shapes.mediumRoundedCorners)
                .border(BorderStroke(1.dp, outlineColor), theme.shapes.mediumRoundedCorners)
                .size(boxSize)
                .background(backgroundColor)
                .clickable { updateState(!checkedState.value) },
        ) {
            if (checkedState.value) Icon(
                Icons.Default.Check,
                tint = if (enabled) colors.checkmarkColor else colors.disabledCheckmarkColor,
                contentDescription = label.orEmpty()
            )
        }
        if (!label.isNullOrBlank()) {
            ProvideTextStyle(value = textStyle) {
                Text(
                    text = label,
                    modifier = Modifier
                        .padding(start = boxSize / 2)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

// MARK: Utils

typealias CheckboxState = SkydioCheckbox.State

/**
 * Provides a more readable variable name than [CheckboxState.value]
 */
val CheckboxState.isChecked: Boolean get() = value

// MARK: Theming

data class CheckboxColors(
    val checkedBackgroundColor: Color,
    val disabledCheckedBackgroundColor: Color,
    val uncheckedBackgroundColor: Color,
    val checkedBorderColor: Color,
    val uncheckedBorderColor: Color,
    val disabledCheckmarkColor: Color,
    val checkmarkColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            checkedBackgroundColor: Color = Blue500,
            disabledCheckedBackgroundColor: Color = Blue700,
            uncheckedBackgroundColor: Color = Transparent,
            checkedBorderColor: Color = Transparent,
            uncheckedBorderColor: Color = when (theme) {
                AppTheme.Dark -> Gray600
                AppTheme.Light -> Gray400
            },
            checkmarkColor: Color = Gray0,
            disabledCheckmarkColor: Color = Gray400
        ) = CheckboxColors(
            checkedBackgroundColor = checkedBackgroundColor,
            disabledCheckedBackgroundColor = disabledCheckedBackgroundColor,
            uncheckedBackgroundColor = uncheckedBackgroundColor,
            checkedBorderColor = checkedBorderColor,
            uncheckedBorderColor = uncheckedBorderColor,
            checkmarkColor = checkmarkColor,
            disabledCheckmarkColor = disabledCheckmarkColor,
        )
    }
}
