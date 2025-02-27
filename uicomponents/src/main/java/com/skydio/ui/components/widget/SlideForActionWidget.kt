package com.skydio.ui.components.widget

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Icon
import com.skydio.ui.components.widget.buttons.SkydioButton
import com.skydio.ui.components.widget.buttons.ButtonState
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SlideForActionWidget(
    text: String,
    slideProgressPercent: Float,
    highlightColor: Color,
    modifier: Modifier = Modifier,
    showSecondaryBtn: Boolean = false,
    secondaryButtonText: String = "",
    theme: AppTheme = getAppTheme(),
    userStartedSlide: () -> Unit = {},
    userEndedSlide: () -> Unit = {},
    onSlideUpdate: (percentChange: Float) -> Unit = {},
    userTappedSecondaryButton: () -> Unit = {}
) {

    val launchSlidePercentage by rememberUpdatedState(slideProgressPercent)

    BoxWithConstraints(
        modifier = modifier
            .padding(2.dp)
            .padding(2.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        theme.colors.defaultWidgetBackgroundColor,
                        theme.colors.defaultContainerBackgroundColor
                    )
                ),
                shape = theme.shapes.mediumRoundedCorners
            )
    ) {
        ActionSlider(
            modifier = Modifier.align(Alignment.CenterStart)
                .fillMaxSize(),
            slideProgressPercent = launchSlidePercentage,
            theme = theme,
            highlightColor = highlightColor,
            sliderText = text,
            onDragStart = userStartedSlide,
            onDragPercentageUpdate = onSlideUpdate,
            onDragEnd = userEndedSlide
        )

        if (showSecondaryBtn) {
            SkydioButton(
                state = ButtonState(
                    label = secondaryButtonText,
                    style = SkydioButton.Style.Secondary
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(2.dp, 0.dp)
                    .align(Alignment.CenterEnd),
                onClick = {
                    userTappedSecondaryButton.invoke()
                }
            )
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.ActionSlider(
    modifier: Modifier = Modifier,
    slideProgressPercent: Float,
    theme: AppTheme,
    highlightColor: Color,
    sliderText: String = "",
    onDragStart: () -> Unit,
    onDragPercentageUpdate: (Float) -> Unit,
    onDragEnd: () -> Unit
) {
    val density = LocalDensity.current
    val thumbSize by remember { derivedStateOf { maxHeight } }
    val totalDragWidth by remember {
        derivedStateOf {
            with(density) { (maxWidth - thumbSize).toPx() }
        }
    }

    val leftWeight = slideProgressPercent.coerceIn(0f, 1f)
    val rightWeight = (1 - slideProgressPercent).coerceIn(0f, 1f)

    val thumbIcon =
        if (slideProgressPercent >= 1f) DrawableImageSource(R.drawable.ic_launch_slide_complete)
        else DrawableImageSource(R.drawable.ic_launch_slide_arrows)

    fun updateDrag(delta: Float) = onDragPercentageUpdate(delta / totalDragWidth)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(2.dp)
            .background(
                color = theme.colors.defaultWidgetBackgroundColor,
                shape = theme.shapes.smallRoundedCorners
            )
            .background(
                color = theme.colors.defaultContainerBackgroundColor,
                shape = theme.shapes.smallRoundedCorners
            )
            .clip(shape = theme.shapes.smallRoundedCorners)
    ) {
        SkydioText(
            text = sliderText,
            style = theme.typography.footnote
        )
    }

    Row {
        Box(
            modifier = Modifier
                .offset(x = 2.dp)
                .fillMaxHeight()
                .let { if (leftWeight == 0f) it.width(0.dp) else it.weight(leftWeight) }
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            highlightColor.copy(alpha = .8f)
                        )
                    )
                ))

        SliderThumb(
            highlightColor = highlightColor,
            modifier = Modifier
                .padding(2.dp)
                .size(thumbSize)
                .graphicsLayer()
                .draggable(
                    state = rememberDraggableState(onDelta = ::updateDrag),
                    orientation = Orientation.Horizontal,
                    onDragStarted = { onDragStart() },
                    onDragStopped = { onDragEnd() }),
            icon = thumbIcon,
            theme = theme
        )

        Spacer(modifier = Modifier.let {
            if (rightWeight == 0f) it.width(0.dp) else it.weight(rightWeight)
        })

    }
}

@Composable
private fun SliderThumb(
    highlightColor: Color,
    modifier: Modifier,
    icon: ImageSource,
    theme: AppTheme
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 1.dp)
                .background(
                    color = theme.colors.appBackgroundColor,
                    shape = theme.shapes.smallRoundedCorners
                )
        )
        Box(
            contentAlignment = Alignment.Center,
            content = { Icon(source = icon, modifier = Modifier.size(12.dp)) },
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = highlightColor,
                    shape = theme.shapes.smallRoundedCorners
                )
        )
    }
}

// MARK: Preview

private object PreviewViewModel {

    val launchSlidePercentage = MutableStateFlow(0f)

    fun userStartedDrag() = Unit

    fun userUpdatedDrag(percentChange: Float) {
        launchSlidePercentage.value = launchSlidePercentage.value + percentChange
    }

    fun userEndedDrag() {
        if (launchSlidePercentage.value < 1f) launchSlidePercentage.value = 0f
    }

}

@Preview
@Composable
private fun WidgetPreview() {
    ThemedPreviews { theme ->
        val progress by PreviewViewModel.launchSlidePercentage.collectAsState()
        SlideForActionWidget(
            text = "Slide for Action",
            slideProgressPercent = progress,
            highlightColor = theme.colors.selectedItemBackground,
            modifier = Modifier.size(width = 200.dp, height = 40.dp),
            theme = theme,
            userStartedSlide = PreviewViewModel::userStartedDrag,
            userEndedSlide = PreviewViewModel::userEndedDrag,
            onSlideUpdate = PreviewViewModel::userUpdatedDrag
        )
    }
}
