package com.skydio.ui.components.widget.toggles

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.widget.buttons.ACTION_BUTTON_HEIGHT
import com.skydio.ui.components.widget.buttons.ACTION_BUTTON_WIDTH
import com.skydio.ui.designsystem.Gray300
import com.skydio.ui.util.extension.noRippleClickable
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SkydioToggleSelector(
    modifier: Modifier = Modifier,
    width: Dp = ACTION_BUTTON_WIDTH,
    height: Dp = ACTION_BUTTON_HEIGHT,
    checkedTrackColor: Color = Color(0xFFFFFFFF),
    uncheckedTrackColor: Color = Color(0xFFFFFFFF),
    thumbSize: Dp = 12.dp,
    leftCheckedImageVector: ImageVector,
    leftUncheckedImageVector: ImageVector,
    onLeftCheckedChanged: (Boolean) -> Unit = {},
    rightCheckedImageVector: ImageVector,
    rightUncheckedImageVector: ImageVector,
    onRightCheckedChanged: (Boolean) -> Unit = {},
    switchStateFlow: MutableStateFlow<Boolean>,
    enabled: Boolean = true
) {

    val switchOn by switchStateFlow.collectAsState()

    // for moving the thumb
    val alignment by animateAlignmentAsState(if (switchOn) 1f else -1f)
    val shapeSize = 2.dp

    // outer rectangle with border
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .noRippleClickable {
                if (enabled) {
                    switchStateFlow.value = !switchStateFlow.value
                    onLeftCheckedChanged(!switchOn)
                    onRightCheckedChanged(switchOn)
                }
            }
            .background(
                color = Color(0xFF3D3D3D),
                shape = RoundedCornerShape(size = shapeSize)
            )
    ) {
        Row {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Icon(
                    imageVector = leftUncheckedImageVector,
                    contentDescription = "",
                    modifier = Modifier.size(size = thumbSize),
                    tint = Color(0XFF838383)
                )
            }
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Icon(
                    imageVector = rightUncheckedImageVector,
                    contentDescription = "",
                    modifier = Modifier.size(size = thumbSize),
                    tint = Color(0XFF838383)
                )
            }
        }
        // this is to add padding at the each horizontal side
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = alignment
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = if (!enabled) Gray300 else {
                            if (switchOn) checkedTrackColor else uncheckedTrackColor
                        },
                        shape = RoundedCornerShape(
                            topStart = if (!switchOn) shapeSize else 0.dp,
                            topEnd = if (switchOn) shapeSize else 0.dp,
                            bottomStart = if (!switchOn) shapeSize else 0.dp,
                            bottomEnd = if (switchOn) shapeSize else 0.dp
                        )
                    )
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight()
            ) {
                // thumb with icon
                Icon(
                    imageVector = if (switchOn) rightCheckedImageVector else leftCheckedImageVector,
                    contentDescription = if (switchOn) "Enabled" else "Disabled",
                    modifier = Modifier.size(size = thumbSize)
                )
            }
        }
    }
}

@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
}