package com.skydio.ui.components.widget.buttons

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Image
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.util.extension.clickableIf
import com.skydio.ui.util.orEmpty

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a button.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioIconButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioIconButton.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    var onClickListener: ButtonClickListener? = null

    enum class Style {
        Primary,
        Secondary,
        Tertiary,
        NoContainer,
        Overlay,
        SecondaryV3
    }

    enum class Size(
        val boxWidth: Dp,
        val boxHeight: Dp,
        val iconSize: Dp
    ) {
        Small(24.dp, 24.dp, 16.dp),
        Medium(32.dp, 32.dp, 16.dp),
        MediumLarge(44.dp, 32.dp, 16.dp),
        Large(48.dp, 48.dp, 24.dp)
    }

    /**
     * The entire state of any given [SkydioIconButton].
     */
    data class State(
        @DrawableRes val icon: Int? = null,
        val style: Style = Style.Primary,
        val size: Size = Size.Medium,
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true,
    ) : BaseViewState()

    @Composable
    override fun Content(state: State) = SkydioIconButton(
        state = state,
        onClick = { onClickListener?.onClick() }
    )

}

// MARK: Composable Impl

@Composable
fun SkydioIconButton(
    state: IconButtonState,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {},
) = SkydioIconButton(
    icon = state.icon?.let(::DrawableImageSource).orEmpty(),
    size = state.size,
    style = state.style,
    enabled = state.isEnabled,
    modifier = modifier,
    theme = theme,
    shape = shape,
    onClick = onClick
)

@Composable
private fun SkydioIconButton(
    icon: ImageSource,
    size: SkydioIconButton.Size,
    style: SkydioIconButton.Style,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {},
) = when (style) {
    SkydioIconButton.Style.Primary ->
        PrimaryIconButton(
            icon = icon,
            size = size,
            modifier = modifier,
            enabled = enabled,
            theme = theme,
            onClick = onClick
        )
    SkydioIconButton.Style.Secondary ->
        SecondaryIconButton(
            icon = icon,
            size = size,
            modifier = modifier,
            enabled = enabled,
            theme = theme,
            onClick = onClick
        )
    SkydioIconButton.Style.SecondaryV3 ->
        SecondaryIconButton(
            icon = icon,
            size = size,
            modifier = modifier,
            enabled = enabled,
            theme = theme,
            shape = shape,
            colors = ButtonColors.secondaryV3ButtonColors(theme),
            onClick = onClick
        )
    SkydioIconButton.Style.Tertiary ->
        TertiaryIconButton(
            icon = icon,
            size = size,
            modifier = modifier,
            enabled = enabled,
            theme = theme,
            onClick = onClick
        )
    SkydioIconButton.Style.NoContainer ->
        NoContainerIconButton(
            icon = icon,
            size = size,
            modifier = modifier,
            enabled = enabled,
            theme = theme,
            onClick = onClick
        )
    SkydioIconButton.Style.Overlay ->
        OverlayIconButton(
            icon = icon,
            size = size,
            modifier = modifier,
            enabled = enabled,
            theme = theme,
            onClick = onClick
        )
}

@Composable
private fun SkydioIconButton(
    icon: ImageSource?,
    size: SkydioIconButton.Size,
    enabled: Boolean,
    modifier: Modifier,
    theme: AppTheme,
    colors: ButtonColors,
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit
) {
    val bgColor =
        if (enabled) colors.backgroundColor
        else colors.disabledBorderColor
    val contentColor =
        if (enabled) colors.contentColor
        else colors.disabledContentColor
    val borderColor =
        if (enabled) colors.borderColor
        else colors.disabledBorderColor
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(size.boxWidth)
            .height(size.boxHeight)
            .background(color = bgColor, shape = shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .clickableIf(enabled, onClick)
    ) {
        icon ?: return@Box
        Image(
            modifier = Modifier.size(size.iconSize),
            source = icon,
            colorFilter = ColorFilter.tint(color = contentColor)
        )
    }
}

@Composable
fun PrimaryIconButton(
    icon: ImageSource,
    size: SkydioIconButton.Size,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {}
) = SkydioIconButton(
    icon = icon,
    size = size,
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    colors = ButtonColors.primaryButtonColors(theme),
    shape = shape,
    onClick = onClick
)

@Composable
fun SecondaryIconButton(
    icon: ImageSource,
    size: SkydioIconButton.Size,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    colors: ButtonColors = ButtonColors.secondaryButtonColors(theme),
    onClick: () -> Unit = {}
) = SkydioIconButton(
    icon = icon,
    size = size,
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    colors = colors,
    shape = shape,
    onClick = onClick
)

@Composable
fun TertiaryIconButton(
    icon: ImageSource,
    size: SkydioIconButton.Size,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {}
) = SkydioIconButton(
    icon = icon,
    size = size,
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    colors = ButtonColors.tertiaryButtonColors(theme),
    shape = shape,
    onClick = onClick
)

@Composable
fun NoContainerIconButton(
    icon: ImageSource,
    size: SkydioIconButton.Size,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {}
) = SkydioIconButton(
    icon = icon,
    size = size,
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    shape = shape,
    colors = ButtonColors.noContainerButtonColors(theme),
    onClick = onClick
)

@Composable
fun OverlayIconButton(
    icon: ImageSource,
    size: SkydioIconButton.Size,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onClick: () -> Unit = {}
) = SkydioIconButton(
    icon = icon,
    size = size,
    enabled = enabled,
    modifier = modifier,
    theme = theme,
    shape = shape,
    colors = ButtonColors.overlayButtonColors(theme),
    onClick = onClick
)

// MARK: Utils

typealias IconButtonState = SkydioIconButton.State

// MARK: Preview/Example

@Preview
@Composable
private fun IconButtonPreview() {
    val baseState = IconButtonState(icon = R.drawable.ic_placeholder_icon)
    ThemedPreviews { theme ->
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SkydioIconButton.Style.values().forEach { style ->
                val styleState = baseState.copy(style = style)
                SkydioIconButton.Size.values().forEach { size ->
                    val sizeState = styleState.copy(size = size)
                    SkydioIconButton(state = sizeState.copy(isEnabled = true), theme = theme)
                    Spacer(modifier = Modifier.width(8.dp))
                    SkydioIconButton(state = sizeState.copy(isEnabled = false), theme = theme)
                }
            }
        }
    }
}
