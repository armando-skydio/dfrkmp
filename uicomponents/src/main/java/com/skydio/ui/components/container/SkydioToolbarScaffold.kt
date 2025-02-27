package com.skydio.ui.components.container

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skydio.ui.components.navigation.NavigationAction
import com.skydio.ui.components.navigation.SkydioToolbar
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme

// TODO(troy): SkydioToolbarScaffold could have UI overlaps - fix it

@Composable
fun SkydioToolbarScaffold(
    modifier: Modifier = Modifier,
    navigationAction: NavigationAction? = null,
    toolbarTitle: String? = null,
    onToolbarNavAction: (NavigationAction) -> Unit = {},
    theme: AppTheme = getAppTheme(),
    content: @Composable BoxScope.() -> Unit,
) {
    Column(modifier = modifier) {
        SkydioToolbar(
            title = toolbarTitle,
            navigationAction = navigationAction,
            onToolbarNavAction = onToolbarNavAction,
            theme = theme)

        Box(
            content = content,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }

}
