package com.skydio.ui.components.widget.quickactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Icon
import com.skydio.ui.components.widget.buttons.SkydioActionButton
import com.skydio.ui.components.widget.text.SkydioMarqueeText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Green400
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews

internal val QUICK_ACTION_WIDTH = 80.dp
internal val QUICK_ACTION_HEIGHT = 44.dp
internal val QUICK_ICON_SIZE = 16.dp

interface SkydioQuickAction {
    val quickActionLabel: String
    val quickActionIcon: ImageSource
    val closePopoutOnQuickAction: Boolean
}

@Composable
internal fun SkydioQuickAction(
    text: String,
    icon: ImageSource,
    onClick: () -> Unit,
    rightContent: @Composable () -> Unit,
    theme: AppTheme = getAppTheme(),
) {
    SkydioActionButton(
        theme = theme,
        onClick = onClick,
        modifier = Modifier
            .width(QUICK_ACTION_WIDTH)
            .height(QUICK_ACTION_HEIGHT)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 4.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f)
            ) {
                Icon(source = icon, modifier = Modifier.size(QUICK_ICON_SIZE), theme = theme)
                SkydioMarqueeText(text = text, style = theme.typography.footnote)
            }
            rightContent()
        }
    }
}

data class SkydioQuickActionColors(
    val selectedPill: Color,
    val unselectedPill: Color
) {

    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            selectedPill: Color = Green400,
            unselectedPill: Color = theme.colors.defaultContainerBackgroundColor
        ) = SkydioQuickActionColors(
            selectedPill = selectedPill,
            unselectedPill = unselectedPill)
    }

}

@Preview
@Composable
private fun SkydioQuickAction() {
    ThemedPreviews { theme ->
        SkydioQuickAction(
            text = "Example",
            icon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            onClick = { },
            rightContent = { },
            theme = theme
        )
    }
}
