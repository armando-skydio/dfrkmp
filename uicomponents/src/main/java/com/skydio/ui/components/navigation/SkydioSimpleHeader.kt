package com.skydio.ui.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.core.Image
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.extension.noRippleClickable

/**
 * Enum for which side of the [SkydioSimpleHeader] the [NavigationAction] is on.
 */
enum class HeaderActionAlignment { START, END }

/**
 * Simple, reusable header for basic needs.
 * Note that this only provides a simple title and navigation action, if you need more complex
 * headers consider using a Toolbar.
 */
@Composable
fun SkydioSimpleHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: NavigationAction? = null,
    showBottomDivider: Boolean = true,
    actionAlignment: HeaderActionAlignment = HeaderActionAlignment.START,
    onActionClicked: (NavigationAction) -> Unit = {},
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.headlineMedium
) {

    @Composable
    fun MaybeAction() {
        if (action != null) Image(
            source = DrawableImageSource(action.icon),
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable { onActionClicked(action) }
        )
    }

    @Composable
    fun MaybeSpacer() {
        if (action != null) Spacer(modifier = Modifier.width(16.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
        ) {
            if (actionAlignment == HeaderActionAlignment.START) {
                MaybeAction()
                MaybeSpacer()
            }

            SkydioText(
                text = title,
                style = textStyle,
                modifier = Modifier.weight(1f))

            if (actionAlignment == HeaderActionAlignment.END) {
                MaybeSpacer()
                MaybeAction()
            }
        }

        if (showBottomDivider) Divider(
            thickness = 1.dp,
            color = theme.colors.dividerColor,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter))
    }
}
