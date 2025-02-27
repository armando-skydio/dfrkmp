package com.skydio.ui.components.widget.quickactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Image
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.extension.noRippleClickable

@Composable
fun SkydioQuickActionHeader(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageSource? = null,
    showSettingsIcon: Boolean = true,
    theme: AppTheme = getAppTheme(),
    onSettingsClicked: (() -> Unit) = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {

            icon?.let {
                Image(
                    source = icon,
                    modifier = Modifier.size(16.dp),
                )
            }

            SkydioText(
                text = title,
                style = theme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (showSettingsIcon) {
                Image(
                    source = DrawableImageSource(R.drawable.ic_settings),
                    modifier = Modifier
                        .size(16.dp)
                        .noRippleClickable { onSettingsClicked() }
                )
            }
        }

        Divider(
            thickness = 1.dp,
            color = theme.colors.dividerColor,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter))
    }
}
