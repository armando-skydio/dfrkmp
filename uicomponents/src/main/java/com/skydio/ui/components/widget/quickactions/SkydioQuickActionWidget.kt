package com.skydio.ui.components.widget.quickactions

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Icon
import com.skydio.ui.components.core.Image
import com.skydio.ui.components.core.SkydioPopup
import com.skydio.ui.components.widget.buttons.ACTION_BUTTON_ICON_SIZE
import com.skydio.ui.components.widget.buttons.ACTION_BUTTON_SIZE
import com.skydio.ui.components.widget.buttons.SkydioActionButton
import com.skydio.ui.components.widget.buttons.SkydioActionButtonColors
import com.skydio.ui.components.widget.quickactions.SkydioQuickActionWidgetState.Companion.Saver
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Green400
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.util.extension.noRippleClickable

enum class QuickActionOpenDirection(
    internal val xOffsetSign: Int,
    internal val yOffsetSign: Int
) {
    LEFT(xOffsetSign = -1, yOffsetSign = 0),
    RIGHT(xOffsetSign = 1, yOffsetSign = 0),
    UP(xOffsetSign = 0, yOffsetSign = -1),
    DOWN(xOffsetSign = 0, yOffsetSign = 1),
    NONE(xOffsetSign = 0, yOffsetSign = 0);

    val isHorizontal: Boolean
        get() = this == LEFT || this == RIGHT
}

enum class QuickActionVerticalOffsetDirection(
    internal val yOffsetSign: Int,
) { UP(-1), DOWN(1), NONE(0) }

@Composable
fun SkydioQuickActionWidget(
    action: SkydioQuickAction? = null,
    actions: List<SkydioQuickAction>,
    icon: ImageSource,
    openDirection: QuickActionOpenDirection,
    showBadge: Boolean = false,
    modifier: Modifier = Modifier,
    state: SkydioQuickActionWidgetState = rememberSkydioQuickActionWidgetState(),
    verticalOffsetDirection: QuickActionVerticalOffsetDirection = QuickActionVerticalOffsetDirection.NONE,
    isActive: Boolean = false,
    badgeColor: Color = Green400,
    isSelected: Boolean = false,
    isDisabled: Boolean = false,
    theme: AppTheme = getAppTheme(),
    colors: SkydioActionButtonColors = SkydioActionButtonColors.defaultThemeColors(theme),
) = SkydioQuickActionWidget(
    icon = icon,
    action = action,
    openDirection = openDirection,
    showBadge = showBadge,
    modifier = modifier,
    state = state,
    verticalOffsetDirection = verticalOffsetDirection,
    isActive = isActive,
    badgeColor = badgeColor,
    isDisabled = isDisabled,
    isSelected = isSelected,
    theme = theme,
    colors = colors,
    contentModifier = Modifier,
) {
    QuickActionsList(actions = actions)
}

@Composable
fun SkydioQuickActionWidget(
    icon: ImageSource,
    action: SkydioQuickAction? = null,
    openDirection: QuickActionOpenDirection,
    showBadge: Boolean,
    modifier: Modifier = Modifier,
    state: SkydioQuickActionWidgetState = rememberSkydioQuickActionWidgetState(),
    verticalOffsetDirection: QuickActionVerticalOffsetDirection = QuickActionVerticalOffsetDirection.NONE,
    isActive: Boolean = false,
    badgeColor: Color = Green400,
    isSelected: Boolean = false,
    isDisabled: Boolean = false,
    theme: AppTheme = getAppTheme(),
    colors: SkydioActionButtonColors = SkydioActionButtonColors.defaultThemeColors(theme),
    contentModifier: Modifier = Modifier,
    properties: PopupProperties = PopupProperties(),
    actionButtonSize: IntSize = IntSize.Zero,
    onPopupDismiss: ()-> Unit = {},
    onFocusRequest: ()-> Unit = {},
    isClickable: Boolean = true,
    content: @Composable SkydioQuickActionWidgetScope.() -> Unit,
) {
    BackHandler(enabled = state.isPopoutOpen) {
        onPopupDismiss()
        state.isPopoutOpen = false
    }

    val selected by rememberUpdatedState(isSelected)
    val isSelectedOrOpen by remember { derivedStateOf { selected || state.isPopoutOpen } }
    var active by remember { mutableStateOf(isActive) }
    LaunchedEffect(isActive) {
        active = isActive
    }

    var buttonSize by remember { mutableStateOf(actionButtonSize) }
    SkydioActionButton(
        modifier = modifier
            .onSizeChanged { buttonSize = it }
            .size(ACTION_BUTTON_SIZE),
        isSelected = isSelectedOrOpen,
        isDisabled = isDisabled,
        theme = theme,
        colors = colors,
        onClick = if (isClickable) {
            {
                when (action) {
                    is SkydioToggleQuickAction -> {
                        if (!isDisabled) {
                            action.onCheckChanged(!active)
                        } else {
                            action.onDisabledTouched()
                        }
                    }
                    else ->
                        state.isPopoutOpen = !state.isPopoutOpen
                }
            }
        } else {
            null
        }
    ) {
        val iconTint = if (isDisabled) theme.colors.disabledTextColor else theme.colors.primaryTextColor
        Icon(
            source = icon, theme = theme, tintColor = iconTint, modifier = Modifier
                .size(ACTION_BUTTON_ICON_SIZE)
                .align(Alignment.Center))

        @Composable
        fun Badge(
            theme: AppTheme,
            align: Alignment = Alignment.TopStart,
        ) {
            var color = if (active) badgeColor else theme.colors.defaultContainerBackgroundColor
            Box(
                modifier = Modifier
                    .align(align)
                    .padding(4.dp)
                    .size(width = 3.dp, height = 24.dp)
                    .background(color = color, shape = theme.shapes.mediumRoundedCorners))
        }

        var openedRotation = 0f
        var closedRotation = 0f
        var iconAlignment = Alignment.Center

        when (openDirection) {
            QuickActionOpenDirection.LEFT -> {
                openedRotation = 0f
                closedRotation = -180f
                iconAlignment = Alignment.BottomStart
            }

            QuickActionOpenDirection.RIGHT -> {
                openedRotation = -180f
                closedRotation = 0f
                iconAlignment = Alignment.BottomEnd
            }

            else -> {}
        }

        val iconRotation = remember { Animatable(openedRotation) }
        LaunchedEffect(state.isPopoutOpen) {
            iconRotation.snapTo(if (state.isPopoutOpen) closedRotation else openedRotation)
            iconRotation.animateTo(if (state.isPopoutOpen) openedRotation else closedRotation)
        }

        if (showBadge) {
            Badge(
                theme = theme,
                align = Alignment.TopStart
            )
        }

        if (openDirection != QuickActionOpenDirection.NONE) {
            Icon(
                source = DrawableImageSource(R.drawable.ic_quick_action_arrow),
                theme = theme,
                tintColor = iconTint,
                modifier = Modifier
                    .padding(3.dp)
                    .size(6.dp)
                    .align(iconAlignment)
                    .rotate(iconRotation.value))
        }

        if (state.isPopoutOpen) QuickActionsPopout(
            modifier = contentModifier,
            actionButtonSize = buttonSize,
            openDirection = openDirection,
            state = state,
            verticalOffsetDirection = verticalOffsetDirection,
            properties = properties,
            theme = theme,
            onDismiss = { state.isPopoutOpen = false
                        onPopupDismiss()},
            content = content,
            onFocusRequest = {
                onFocusRequest()
            }
        )

        // prevents double dismiss clicks
        if (state.isPopoutOpen) Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .noRippleClickable())
    }
}


/**
 * Note: overridePopupOffsetX is only set when this QuickActionsPopout is directly used
 * instead of embedded in a SkydioQuickActionWidget so that we know how much is the popup-initiating button offsetted
 */
@Composable
fun QuickActionsPopout(
    modifier: Modifier = Modifier,
    actionButtonSize: IntSize,
    openDirection: QuickActionOpenDirection,
    state: SkydioQuickActionWidgetState,
    verticalOffsetDirection: QuickActionVerticalOffsetDirection,
    properties: PopupProperties = PopupProperties(),
    overridePopupOffsetX: Int? = null,
    theme: AppTheme = getAppTheme(),
    onDismiss: () -> Unit = {},
    onFocusRequest: () -> Unit = {},
    content: @Composable SkydioQuickActionWidgetScope.() -> Unit
) {
    val connectorArrowSize = DpSize(width = 14.dp, height = 24.dp)
    // If image is rotated 90/270 degrees (for vertical open directions), the Image layout bounds
    // will remain the same but the content bounds will change, so we need to offset Y by the height/width delta
    val connectorArrowOffsetY: Int = with(LocalDensity.current) {
        return@with if (openDirection.isHorizontal)
            0.dp.roundToPx()
        else
            ((connectorArrowSize.height - connectorArrowSize.width) / 2).roundToPx()
    }

    val popoutButtonSpacing = with(LocalDensity.current) { (4.dp).roundToPx() }

    var popupSize by remember { mutableStateOf(IntSize.Zero) }

    val popupOffset by remember {
        derivedStateOf {
            if (openDirection.isHorizontal) {
                val x = popoutButtonSpacing + (popupSize.width / 2) + (actionButtonSize.width / 2)
                val y = popupSize.height / 4
                IntOffset(x * openDirection.xOffsetSign, y * verticalOffsetDirection.yOffsetSign)
            } else {
                val x = overridePopupOffsetX ?: (popupSize.width / 4)
                val y = popoutButtonSpacing + (popupSize.height / 2) + (actionButtonSize.height / 2) + connectorArrowOffsetY
                IntOffset(x, y * openDirection.yOffsetSign)
            }
        }
    }

    @Composable
    fun ConnectorArrow() {
        val connectorArrowRotation = when (openDirection) {
            QuickActionOpenDirection.LEFT -> 0f
            QuickActionOpenDirection.RIGHT -> 180f
            QuickActionOpenDirection.UP -> 90f
            QuickActionOpenDirection.DOWN -> 270f
            QuickActionOpenDirection.NONE -> 0f
        }

        Image(
            source = DrawableImageSource(R.drawable.popout_arrow),
            colorFilter = ColorFilter.tint(theme.colors.defaultContainerBackgroundLight),
            modifier = Modifier
                .size(connectorArrowSize)
                .absoluteOffset {
                    if (openDirection.isHorizontal)
                        IntOffset(x = 0, y = -popupOffset.y)
                    else
                        IntOffset(
                            x = (actionButtonSize.width / 4),
                            y = openDirection.yOffsetSign * connectorArrowOffsetY
                        )
                }
                .rotate(connectorArrowRotation)
        )
    }

    SkydioPopup(
        alignment = Alignment.Center,
        offset = popupOffset,
        properties = properties,
        onDismissRequest = { onDismiss() },
    ) {
        rememberScrollState()
        if (openDirection.isHorizontal) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.onSizeChanged { popupSize = it }
            ) {
                if (openDirection == QuickActionOpenDirection.RIGHT) ConnectorArrow()
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .absoluteOffset(x = (-1).dp * openDirection.xOffsetSign) // fuse content and connector arrow together
                        .background(
                            color = theme.colors.defaultContainerBackgroundLight,
                            shape = theme.shapes.mediumRoundedCorners
                        )
                        .padding(2.dp)
                ) {
                    val scope = SkydioQuickActionWidgetScope(
                        parent = this,
                        state = state,
                        theme = theme)
                    with(scope) { content() }
                }
                if (openDirection == QuickActionOpenDirection.LEFT) ConnectorArrow()
            }
        } else {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .onSizeChanged { popupSize = it }
            ) {
                if (openDirection == QuickActionOpenDirection.DOWN) ConnectorArrow() // fuse content and connector arrow together
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .absoluteOffset(y = (-1).dp * openDirection.yOffsetSign)
                        .background(
                            color = theme.colors.defaultContainerBackgroundLight,
                            shape = theme.shapes.mediumRoundedCorners
                        )
                        .padding(2.dp)
                ) {
                    val scope = SkydioQuickActionWidgetScope(
                        parent = this,
                        state = state,
                        theme = theme)
                    with(scope) { content() }
                }
                if (openDirection == QuickActionOpenDirection.UP) ConnectorArrow()
            }
        }
    }
}

// MARK: State

@Composable
fun rememberSkydioQuickActionWidgetState(isOpen: Boolean = false): SkydioQuickActionWidgetState =
    rememberSaveable(saver = Saver) { SkydioQuickActionWidgetState(isOpen) }

class SkydioQuickActionWidgetState(initial: Boolean) {

    var isPopoutOpen: Boolean by mutableStateOf(initial)

    companion object {
        internal val Saver: Saver<SkydioQuickActionWidgetState, *> = Saver(
            save = { it.isPopoutOpen },
            restore = { SkydioQuickActionWidgetState(it) })
    }
}

// MARK: Scope

class SkydioQuickActionWidgetScope internal constructor(
    private val parent: ColumnScope,
    private val state: SkydioQuickActionWidgetState,
    private val theme: AppTheme
) : ColumnScope {

    override fun Modifier.align(alignment: Alignment.Horizontal): Modifier =
        with(parent) { align(alignment) }

    override fun Modifier.alignBy(alignmentLineBlock: (Measured) -> Int): Modifier =
        with(parent) { alignBy(alignmentLineBlock) }

    override fun Modifier.alignBy(alignmentLine: VerticalAlignmentLine): Modifier =
        with(parent) { alignBy(alignmentLine) }

    override fun Modifier.weight(weight: Float, fill: Boolean): Modifier =
        with(parent) { weight(weight, fill) }

    @Composable
    fun QuickActionsList(actions: List<SkydioQuickAction>) {
        actions.forEach { action ->
            when (action) {
                is SkydioButtonQuickAction ->
                    SkydioQuickActionButton(action = action, widgetState = state, theme = theme)

                is SkydioLinkQuickAction ->
                    SkydioQuickActionLink(action = action, widgetState = state, theme = theme)

                is SkydioPickerQuickAction<*> ->
                    SkydioQuickActionPicker(action = action, widgetState = state, theme = theme)

                is SkydioToggleQuickAction ->
                    SkydioQuickActionToggle(action = action, widgetState = state, theme = theme)

                else ->{

                }
            }
        }
    }

}

@Preview
@Composable
private fun SkydioQuickActionWidgetPreview() {
    ThemedPreviews { theme ->
        SkydioQuickActionWidget(
            actions = emptyList(),
            icon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            openDirection = QuickActionOpenDirection.RIGHT,
            theme = theme,
            isSelected = false,
            isActive = true,
        )
        SkydioQuickActionWidget(
            actions = emptyList(),
            icon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            openDirection = QuickActionOpenDirection.RIGHT,
            theme = theme,
            isSelected = true,
            isActive = true,
        )
    }
}
