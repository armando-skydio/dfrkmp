package com.skydio.ui.components.widget.buttons

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Icon
import com.skydio.ui.components.core.Image
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a button.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioButton.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    var onClickListener: ButtonClickListener? = null

    enum class Style { Primary, Secondary, Destructive, Tertiary, TertiaryNoTinted }

    enum class Size(
        internal val height: Dp,
        internal val iconSize: Dp,
        internal val contentPadding: PaddingValues,
    ) {
        Small(24.dp, 16.dp, PaddingValues(horizontal = 8.dp, vertical = 4.dp)),
        Medium(32.dp, 16.dp, PaddingValues(horizontal = 8.dp, vertical = 4.dp)),
        Large(44.dp, 24.dp, ButtonDefaults.ContentPadding)
    }

    /**
     * The entire state of any given [SkydioButton].
     */
    data class State(
        val label: String? = null,
        val style: Style = Style.Primary,
        val size: Size = Size.Medium,
        val leftIcon: ImageSource? = null,
        val rightIcon: ImageSource? = null,
        val startIcon: ImageSource? = null,
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true,
    ) : BaseViewState()

    @Composable
    override fun Content(state: State) = SkydioButton(
        state = state,
        onClick = { onClickListener?.onClick() }
    )

}

// MARK: Composable Impl

@Composable
fun SkydioButton(
    state: ButtonState,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = getAppTheme().typography.body,
    customTextColor: Color? = null,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {},
) = SkydioButton(
    label = state.label,
    leftIcon = state.leftIcon,
    rightIcon = state.rightIcon,
    startIcon = state.startIcon,
    style = state.style,
    enabled = state.isEnabled,
    modifier = modifier,
    size = state.size,
    theme = theme,
    textStyle = textStyle,
    shape = shape,
    customTextColor = customTextColor,
    onClick = onClick,
)

@Composable
fun SmallSkydioButton(
    state: ButtonState,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = getAppTheme().typography.bodyBold,
    customTextColor: Color? = null,
    theme: AppTheme = getAppTheme(),
    onClick: () -> Unit = {},
) = SkydioButton(
    label = state.label,
    leftIcon = state.leftIcon,
    rightIcon = state.rightIcon,
    style = state.style,
    enabled = state.isEnabled,
    modifier = modifier,
    size = SkydioButton.Size.Small,
    theme = theme,
    textStyle = textStyle,
    customTextColor = customTextColor,
    onClick = onClick,
)

@Composable
fun SkydioButton(
    label: String?,
    style: SkydioButton.Style,
    size: SkydioButton.Size = SkydioButton.Size.Medium,
    contentPadding: PaddingValues = size.contentPadding,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    leftIcon: ImageSource? = null,
    rightIcon: ImageSource? = null,
    startIcon: ImageSource? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    customTextColor: Color? = null,
    onClick: () -> Unit = {},
) = when (style) {
    SkydioButton.Style.Primary ->
        PrimaryButton(
            label.orEmpty(),
            modifier,
            size,
            contentPadding = contentPadding,
            leftIcon,
            rightIcon,
            startIcon,
            enabled,
            theme,
            textStyle,
            shape,
            customTextColor,
            onClick
        )
    SkydioButton.Style.Secondary ->
        SecondaryButton(
            label.orEmpty(),
            modifier,
            size,
            contentPadding = contentPadding,
            leftIcon,
            rightIcon,
            startIcon,
            enabled,
            theme,
            textStyle,
            shape,
            customTextColor,
            onClick
        )
    SkydioButton.Style.Destructive ->
        DestructiveButton(
            label.orEmpty(),
            modifier,
            size,
            contentPadding = contentPadding,
            leftIcon,
            rightIcon,
            startIcon,
            enabled,
            theme,
            textStyle,
            shape,
            customTextColor,
            onClick
        )
    SkydioButton.Style.Tertiary ->
        TertiaryButton(
            label.orEmpty(),
            modifier,
            size,
            contentPadding = contentPadding,
            leftIcon,
            rightIcon,
            startIcon,
            enabled,
            theme,
            ButtonColors.tertiaryButtonColors(theme),
            theme.typography.bodyBold,
            shape,
            onClick
        )
    SkydioButton.Style.TertiaryNoTinted ->
        TertiaryNoTintedButton(
            label.orEmpty(),
            modifier,
            size,
            contentPadding = contentPadding,
            leftIcon,
            rightIcon,
            startIcon,
            enabled,
            theme,
            ButtonColors.tertiaryButtonColors(theme),
            theme.typography.bodyBold,
            shape,
            onClick
        )
}

@Composable
fun SkydioButton(
    label: String,
    modifier: Modifier,
    size: SkydioButton.Size = SkydioButton.Size.Medium,
    contentPadding: PaddingValues = size.contentPadding,
    colors: ButtonColors,
    leftIcon: ImageSource? = null,
    rightIcon: ImageSource? = null,
    startIcon: ImageSource? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyBold,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    customTextColor: Color? = null,
    onClick: () -> Unit
) {
    val mainButtonColor = ButtonDefaults.buttonColors(
        containerColor = colors.backgroundColor,
        disabledContainerColor = colors.disabledBackgroundColor,
        contentColor = colors.contentColor,
        disabledContentColor = colors.disabledContentColor
    )
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = mainButtonColor,
        contentPadding = contentPadding,
        modifier = Modifier
            .height(size.height)
            .then(modifier)
    ) {
        val textColor = customTextColor ?: if (enabled) colors.contentColor else colors.disabledContentColor
        startIcon?.let {
            Icon(
                source = startIcon,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(size.iconSize)
                    .width(size.iconSize)
                    .align(Alignment.CenterVertically)
            )
        }
        ProvideTextStyle(value = textStyle.copy(color = textColor)) {
            leftIcon?.let {
                Image(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .height(size.iconSize)
                        .width(size.iconSize)
                        .align(Alignment.CenterVertically),
                    source = leftIcon,
                    colorFilter = ColorFilter.tint(color = textColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            val textModifier = if (startIcon != null) Modifier.weight(1f) else Modifier
            Text(text = label, textAlign = TextAlign.Center, modifier = textModifier, maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            rightIcon?.let {
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .height(size.iconSize)
                        .width(size.iconSize)
                        .align(Alignment.CenterVertically),
                    source = rightIcon,
                    colorFilter = ColorFilter.tint(color = textColor)
                )
            }
        }
    }
}

@Composable
fun PrimaryButton(
    label: String,
    modifier: Modifier = Modifier,
    size: SkydioButton.Size = SkydioButton.Size.Medium,
    contentPadding: PaddingValues = size.contentPadding,
    leftIcon: ImageSource? = null,
    rightIcon: ImageSource? = null,
    startIcon: ImageSource? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyBold,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    customTextColor: Color? = null,
    onClick: () -> Unit = {}
) = SkydioButton(
    label = label,
    size = size,
    contentPadding = contentPadding,
    leftIcon = leftIcon,
    rightIcon = rightIcon,
    startIcon = startIcon,
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    textStyle = textStyle,
    shape = shape,
    customTextColor = customTextColor,
    colors = ButtonColors.primaryButtonColors(theme),
    onClick = onClick
)

@Composable
fun SecondaryButton(
    label: String,
    modifier: Modifier = Modifier,
    size: SkydioButton.Size = SkydioButton.Size.Medium,
    contentPadding: PaddingValues = size.contentPadding,
    leftIcon: ImageSource? = null,
    rightIcon: ImageSource? = null,
    startIcon: ImageSource? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyBold,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    customTextColor: Color? = null,
    onClick: () -> Unit = {}
) = SkydioButton(
    label = label,
    size = size,
    contentPadding = contentPadding,
    leftIcon = leftIcon,
    rightIcon = rightIcon,
    startIcon = startIcon,
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    textStyle = textStyle,
    shape = shape,
    customTextColor = customTextColor,
    colors = ButtonColors.secondaryButtonColors(theme),
    onClick = onClick
)

@Composable
fun DestructiveButton(
    label: String,
    modifier: Modifier = Modifier,
    size: SkydioButton.Size = SkydioButton.Size.Medium,
    contentPadding: PaddingValues = size.contentPadding,
    leftIcon: ImageSource? = null,
    rightIcon: ImageSource? = null,
    startIcon: ImageSource? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyBold,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    customTextColor: Color? = null,
    onClick: () -> Unit = {}
) = SkydioButton(
    label = label,
    size = size,
    contentPadding = contentPadding,
    leftIcon = leftIcon,
    rightIcon = rightIcon,
    startIcon = startIcon,
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    textStyle = textStyle,
    shape = shape,
    customTextColor = customTextColor,
    colors = ButtonColors.destructiveButtonColors(theme),
    onClick = onClick
)

@Composable
fun TertiaryButton(
    label: String,
    modifier: Modifier = Modifier,
    size: SkydioButton.Size = SkydioButton.Size.Medium,
    contentPadding: PaddingValues = size.contentPadding,
    leftIcon: ImageSource? = null,
    rightIcon: ImageSource? = null,
    startIcon: ImageSource? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    colors: ButtonColors = ButtonColors.tertiaryButtonColors(theme),
    textStyle: TextStyle = theme.typography.bodyBold,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {}
) {
    val mainButtonColor = ButtonDefaults.outlinedButtonColors(
        contentColor = colors.contentColor,
        disabledContentColor = colors.disabledContentColor
    )

    val borderColor = if (enabled) colors.borderColor else colors.disabledBorderColor

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.height(size.height),
        colors = mainButtonColor,
        contentPadding = contentPadding
    ) {

        startIcon?.let {
            Icon(
                source = startIcon,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(size.height)
                    .width(size.height)
                    .align(Alignment.CenterVertically)
            )
        }

        val textColor = if (enabled) colors.contentColor else colors.disabledContentColor
        ProvideTextStyle(textStyle.copy(color = textColor)) {
            leftIcon?.let {
                Image(
                    modifier = Modifier
                        .height(size.height)
                        .width(size.height)
                        .align(Alignment.CenterVertically),
                    source = leftIcon,
                    colorFilter = ColorFilter.tint(color = textColor)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            val textModifier = if (startIcon != null) Modifier.weight(1f) else Modifier
            Text(text = label, textAlign = TextAlign.Center, modifier = textModifier)
            Spacer(modifier = Modifier.width(8.dp))

            rightIcon?.let {
                Image(
                    modifier = Modifier
                        .height(size.height)
                        .width(size.height)
                        .align(Alignment.CenterVertically),
                    source = rightIcon,
                    colorFilter = ColorFilter.tint(color = textColor)
                )
            }
        }
    }
}

@Composable
fun TertiaryNoTintedButton(
    label: String,
    modifier: Modifier = Modifier,
    size: SkydioButton.Size = SkydioButton.Size.Medium,
    contentPadding: PaddingValues = size.contentPadding,
    leftIcon: ImageSource? = null,
    rightIcon: ImageSource? = null,
    startIcon: ImageSource? = null,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    colors: ButtonColors = ButtonColors.tertiaryButtonColors(theme),
    textStyle: TextStyle = theme.typography.bodyBold,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {}
) {
    val mainButtonColor = ButtonDefaults.outlinedButtonColors(
        contentColor = colors.contentColor,
        disabledContentColor = colors.disabledContentColor
    )

    val borderColor = if (enabled) colors.borderColor else colors.disabledBorderColor

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.height(size.height),
        colors = mainButtonColor,
        contentPadding = contentPadding
    ) {

        startIcon?.let {
            Icon(
                source = startIcon,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(size.iconSize)
                    .width(size.iconSize)
                    .align(Alignment.CenterVertically)
            )
        }

        val textColor = if (enabled) colors.contentColor else colors.disabledContentColor
        ProvideTextStyle(textStyle.copy(color = textColor)) {
            leftIcon?.let {
                Image(
                    modifier = Modifier
                        .height(size.iconSize)
                        .width(size.iconSize)
                        .align(Alignment.CenterVertically),
                    source = leftIcon
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            val textModifier = if (startIcon != null) Modifier.weight(1f) else Modifier.weight(1f)
            Text(text = label, textAlign = TextAlign.Center, modifier = textModifier)
            Spacer(modifier = Modifier.width(8.dp))

            rightIcon?.let {
                Image(
                    modifier = Modifier
                        .height(size.height)
                        .width(size.height)
                        .align(Alignment.CenterVertically),
                    source = rightIcon
                )
            }
        }

    }
}

// MARK: Utils

typealias ButtonState = SkydioButton.State


// MARK: Preview/Example

@Preview
@Composable
private fun ButtonPreview() {
    val baseState = ButtonState(label = "Button")
    ThemedPreviews { theme ->
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SkydioButton.Style.values().forEach { style ->
                val styleState = baseState.copy(style = style)
                // text only
                Row {
                    SkydioButton(state = styleState.copy(isEnabled = true), theme = theme, textStyle = theme.typography.bodyBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    SkydioButton(state = styleState.copy(isEnabled = false), theme = theme, textStyle = theme.typography.bodyBold)
                }
                // icon + text
                Row {
                    val leftIconStyle = styleState.copy(leftIcon = DrawableImageSource(R.drawable.ic_placeholder_icon))
                    SkydioButton(state = leftIconStyle.copy(isEnabled = true), theme = theme, textStyle = theme.typography.bodyBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    SkydioButton(state = leftIconStyle.copy(isEnabled = false), theme = theme, textStyle = theme.typography.bodyBold)
                }
                // text + icon
                Row {
                    val rightIconStyle = styleState.copy(rightIcon = DrawableImageSource(R.drawable.ic_placeholder_icon))
                    SkydioButton(state = rightIconStyle.copy(isEnabled = true), theme = theme, textStyle = theme.typography.bodyBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    SkydioButton(state = rightIconStyle.copy(isEnabled = false), theme = theme, textStyle = theme.typography.bodyBold)
                }
            }
        }
    }
}
