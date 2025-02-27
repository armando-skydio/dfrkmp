package com.skydio.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Icon
import com.skydio.ui.components.core.Image
import com.skydio.ui.components.core.ScrollbarColumn
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.util.extension.clickableIf
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.col

// MARK: Composable Impl

@Composable
fun SkydioSideNavigation(
    modifier: Modifier = Modifier,
    // content
    headerAction: NavigationAction? = null,
    onHeaderActionClicked: ((NavigationAction) -> Unit)? = null,
    navigationGroups: List<List<NavigationItem>> = emptyList(),
    // styling
    mode: SidebarMode = SidebarMode.ICON_AND_LABEL,
    theme: AppTheme = getAppTheme(),
    colors: SideNavigationColors = SideNavigationColors.defaultThemeColors(theme),
    // callbacks
    onNavigationItemClicked: ((NavigationItem) -> Unit)? = null,
) {
    // allow the navigation to be included in other composables, but hidden
    if (mode == SidebarMode.HIDDEN) return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(color = colors.backgroundColor)
    ) {
        headerAction?.let {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                val alignment =
                    if (mode == SidebarMode.ICON_AND_LABEL) Alignment.CenterStart
                    else Alignment.Center
                Box(modifier = Modifier
                    .align(alignment)
                    .padding(horizontal = 10.dp)
                    .size(32.dp)
                    .clickableIf(onHeaderActionClicked != null) {
                        onHeaderActionClicked?.invoke(headerAction)
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center),
                        source = DrawableImageSource(headerAction.icon),
                        theme = theme)
                }

                Divider(
                    color = theme.colors.dividerColor,
                    modifier = Modifier.align(Alignment.BottomCenter))

            }

        }
        navigationGroups.filter { it.isNotEmpty() }.forEachIndexed { index, group ->

            if (index > 0 && group.isNotEmpty()) Divider(color = theme.colors.dividerColor)

            ScrollbarColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                group.forEach { navItem ->
                    if (mode == SidebarMode.ICON_AND_LABEL) {
                        SkydioSidebarNavItem(
                            icon = navItem.icon,
                            label = navItem.label,
                            isSelected = navItem.isSelected,
                            isEnabled = navItem.isEnabled,
                            onItemClicked = onNavigationItemClicked?.let { { it(navItem) } },
                            colors = colors,
                            shape = theme.shapes.mediumRoundedCorners,
                            textStyle = theme.typography.body
                        )
                    } else {
                        SkydioSidebarNavItem(
                            icon = navItem.icon,
                            isSelected = navItem.isSelected,
                            isEnabled = navItem.isEnabled,
                            onItemClicked = onNavigationItemClicked?.let { { it(navItem) } },
                            colors = colors,
                            shape = theme.shapes.mediumRoundedCorners,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkydioSidebarNavItem(
    icon: ImageSource,
    label: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onItemClicked: (() -> Unit)?,
    colors: SideNavigationColors,
    shape: Shape,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    val backgroundColor =
        if (isSelected) colors.selectedItemBackgroundColor
        else Color.Transparent
    val outlineColor =
        if (isSelected) colors.selectedItemOutlineColor
        else Color.Transparent
    val textAndIconColor = when {
        isEnabled -> colors.textAndIconColor
        else -> colors.disabledTextAndIconColor
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(color = backgroundColor, shape = shape)
            .border(width = 1.dp, color = outlineColor, shape = shape)
            .clickableIf(isEnabled && onItemClicked != null) { onItemClicked?.invoke() }
            .padding(start = 19.dp),
    ) {
        Image(
            source = icon,
            colorFilter = ColorFilter.tint(color = textAndIconColor),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            style = textStyle.copy(color = textAndIconColor),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun SkydioSidebarNavItem(
    icon: ImageSource,
    isSelected: Boolean,
    isEnabled: Boolean,
    onItemClicked: (() -> Unit)?,
    colors: SideNavigationColors,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    val backgroundColor =
        if (isSelected) colors.selectedItemBackgroundColor
        else Color.Transparent
    val outlineColor =
        if (isSelected) colors.selectedItemOutlineColor
        else Color.Transparent
    val textAndIconColor = when {
        isEnabled -> colors.textAndIconColor
        else -> colors.disabledTextAndIconColor
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .border(width = 1.dp, color = outlineColor, shape = shape)
            .clickableIf(isEnabled && onItemClicked != null) { onItemClicked?.invoke() }
            .size(width = 54.dp, height = 32.dp),
    ) {
        Image(
            source = icon,
            colorFilter = ColorFilter.tint(color = textAndIconColor),
            modifier = Modifier.size(16.dp)
        )
    }
}

// MARK: Theming

enum class SidebarMode {
    ICON_ONLY,
    ICON_AND_LABEL,
    HIDDEN
}

data class SideNavigationColors(
    val backgroundColor: Color,
    val groupDividerColor: Color,
    val selectedItemBackgroundColor: Color,
    val selectedItemOutlineColor: Color,
    val textAndIconColor: Color,
    val disabledTextAndIconColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            backgroundColor: Color = theme.colors.defaultContainerBackgroundColor,
            groupDividerColor: Color = theme.colors.dividerColor,
            selectedItemBackgroundColor: Color = theme.colors.selectedItemBackground,
            selectedItemOutlineColor: Color = theme.colors.selectedItemBackground,
            textAndIconColor: Color = theme.colors.primaryTextColor,
            disabledTextAndIconColor: Color = theme.colors.disabledTextColor,
        ) = SideNavigationColors(
            backgroundColor = backgroundColor,
            groupDividerColor = groupDividerColor,
            selectedItemBackgroundColor = selectedItemBackgroundColor,
            selectedItemOutlineColor = selectedItemOutlineColor,
            textAndIconColor = textAndIconColor,
            disabledTextAndIconColor = disabledTextAndIconColor,
        )
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun SidebarPreview() {
    val dummyItem = NavigationItem(
        uid = "dummy-item",
        label = "Dummy Item",
        icon = DrawableImageSource(R.drawable.ic_placeholder_icon)
    )
    val groups = listOf(
        listOf(dummyItem, dummyItem, dummyItem.copy(isSelected = true)),
        listOf(dummyItem, dummyItem, dummyItem.copy(isEnabled = false))
    )

    ThemedPreviews { theme ->
        Row(modifier = Modifier.height(400.dp)) {
            SkydioSideNavigation(
                modifier = Modifier.width(2.col),
                headerAction = NavigationAction.CLOSE,
                navigationGroups = groups,
                mode = SidebarMode.ICON_AND_LABEL,
                theme = theme
            )


            Spacer(modifier = Modifier.width(16.dp))

            SkydioSideNavigation(
                modifier = Modifier.width(60.dp),
                headerAction = NavigationAction.CLOSE,
                navigationGroups = groups,
                mode = SidebarMode.ICON_ONLY,
                theme = theme
            )

        }
    }
}
