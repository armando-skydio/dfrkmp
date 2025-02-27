package com.skydio.ui.components.container

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.container.CardColors.Companion.defaultThemeColors
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.components.widget.buttons.PrimaryButton
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Gray800
import com.skydio.ui.designsystem.getAppTheme

// MARK: Composable Impl

@Composable
fun SkydioCard(
    headerText: String,
    modifier: Modifier = Modifier,
    // styling
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    colors: CardColors = defaultThemeColors(theme),
    headerStyle: TextStyle = theme.typography.bodyLarge,
    headerColor: Color = theme.colors.primaryTextColor,
    // child content
    content: @Composable () -> Unit,
) = SkydioCard(
    modifier = modifier,
    theme = theme,
    shape = shape,
    colors = colors
) {
    Column(modifier = Modifier.wrapContentHeight()) {
        SkydioText(text = headerText, style = headerStyle.copy(color = headerColor))
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun SkydioCard(
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    colors: CardColors = defaultThemeColors(theme),
    contentAlignment: Alignment = Alignment.TopStart,
    padding: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit,
) = Box(
    modifier = modifier
        .background(color = colors.backgroundColor, shape = shape)
        .border(width = 1.dp, color = colors.outlineColor, shape = shape)
        .padding(padding),
    contentAlignment = contentAlignment
) {
    content()
}

// MARK : Theming

data class CardColors(
    val backgroundColor: Color,
    val outlineColor: Color,
) {
    companion object {

        fun defaultThemeColors(
            theme: AppTheme,
            backgroundColor: Color = Color(0xFF262626),
            outlineColor: Color = Gray800,
        ) = CardColors(
            backgroundColor = backgroundColor,
            outlineColor = outlineColor,
        )

        fun outlineOnlyCardColors(
            theme: AppTheme,
            backgroundColor: Color = Color.Transparent,
            outlineColor: Color = theme.colors.defaultWidgetOutlineColor,
        ) = CardColors(
            backgroundColor = backgroundColor,
            outlineColor = outlineColor,
        )

        fun selectedCardColors(
            theme: AppTheme,
            backgroundColor: Color = theme.colors.selectedItemBackground,
            outlineColor: Color = theme.colors.defaultWidgetOutlineColor,
        ) = CardColors(
            backgroundColor = backgroundColor,
            outlineColor = outlineColor,
        )
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun PreviewCard() {
    ThemedPreviews { theme ->
        SkydioCard(
            headerText = "Header",
            theme = theme,
            modifier = Modifier
                .padding(8.dp)
                .width(212.dp)
        ) {
            PrimaryButton(label = "Button", theme = theme)
        }
    }
}
