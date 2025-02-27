package com.skydio.ui.components.container

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.widget.sliders.SkydioSliderWithLabel
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ThemedPreviews

@Composable
fun ExpandableSkydioCard(
    modifier: Modifier = Modifier,
    headerContent: @Composable () -> Unit,
    isExpanded: Boolean = false,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    colors: CardColors = CardColors.defaultThemeColors(theme),
    expandedContent: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    SkydioCard(
        modifier = modifier,
        shape = shape,
        colors = colors,
        theme = theme
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) { headerContent() }
                IconButton(onClick = { expanded = !expanded }) {

                }
            }

            if (expanded)
                Box(modifier = Modifier.fillMaxWidth()) { expandedContent() }
        }
    }
}

@Preview
@Composable
private fun SkydioExpandableCardPreview() {
    ThemedPreviews { theme ->
        ExpandableSkydioCard(
            isExpanded = false,
            headerContent = { SkydioText(text = "Collapsed", style = theme.typography.bodyLarge) },
            expandedContent = {},
            theme = theme,
            modifier = Modifier.padding(8.dp))

        ExpandableSkydioCard(
            isExpanded = true,
            headerContent = { SkydioText(text = "Expanded", style = theme.typography.bodyLarge) },
            expandedContent = {
                Column {

                    Spacer(modifier = Modifier.height(5.dp))
                    SkydioSliderWithLabel(
                        label = "Test Slider",
                        min = 0f,
                        max = 100f,
                        value = 50f,
                        stepSize = 0.5f,
                        displayPrecision = 1,
                        style = theme.typography.bodyLarge
                    )
                }
            },
            theme = theme,
            modifier = Modifier.padding(8.dp))
    }
}