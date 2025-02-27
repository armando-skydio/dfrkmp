package com.skydio.ui.components.widget.text

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Image
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Yellow500
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource

@Composable
fun Warning(
    copy: String,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.footnote,
    textColor: Color = theme.colors.primaryTextColor
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .border(
                width = 1.dp,
                color = theme.colors.defaultWidgetOutlineColor,
                shape = theme.shapes.mediumRoundedCorners)
            .clip(shape = theme.shapes.mediumRoundedCorners)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(32.dp)
                .fillMaxHeight()
                .background(color = Yellow500)
        ) {
            Image(
                modifier = Modifier.size(14.dp),
                source = DrawableImageSource(R.drawable.ic_warning_filled),
                colorFilter = ColorFilter.tint(theme.colors.appBackgroundColor))
        }

        SkydioText(
            modifier = Modifier
                .padding(8.dp),
            text = copy,
            style = textStyle,
            color = textColor)
    }
}
