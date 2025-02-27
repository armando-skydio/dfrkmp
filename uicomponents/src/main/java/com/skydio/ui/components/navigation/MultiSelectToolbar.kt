package com.skydio.ui.components.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydio.ui.components.R
import com.skydio.ui.designsystem.SkydioTypography.Companion.case

@Composable
fun MultiSelectToolbar(
    title: String,
    menuItems: SnapshotStateList<MenuOption> = SnapshotStateList(),
    inSelectionMode: Boolean,
    showSelectOption: Boolean = true,
    onMenuClick: (menuOption: MenuOption) -> Unit,
    backgroundBrush: Brush = Brush.verticalGradient(
        listOf(
            Color.Black,
            Color.Black,
        ),
    ),
    selectedBrush: Brush = Brush.verticalGradient(
        listOf(
        ),
    ),
    capitalizeTitle: Boolean = true,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(brush = if (inSelectionMode) selectedBrush else backgroundBrush)
            .padding(horizontal = 12.dp),
    ) {
        val context = LocalContext.current
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = stringResource(id = Back().title),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 6.dp)
                .clickable {
                    onMenuClick.invoke(Back())
                },
        )
        if (inSelectionMode.not()) {
            Text(
                text = if (capitalizeTitle) title.uppercase() else title,
                color = Color.White,
                fontSize = 17.25.sp,
                fontFamily = case,
                fontWeight = FontWeight(450),
                modifier = Modifier.align(Alignment.Center),
            )
            if (showSelectOption) {
                Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                    MenuText(Select(), onMenuClick, true)
                }
            }
        } else {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                for (menuItem in menuItems) {
                    if (menuItem.visible.not()) {
                        continue
                    }
                    MenuText(menuItem, onMenuClick, menuItem.active)
                }
            }
        }
    }
}

@Composable
private fun MenuText(
    menuOption: MenuOption,
    onMenuClick: (menuOption: MenuOption) -> Unit,
    active: Boolean,
) {
    Text(
        text = stringResource(id = menuOption.title),
        color = if (active) Color.White else Color.White.copy(alpha = 0.5F),
        fontSize = 16.sp,
        fontFamily = case,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                if (menuOption.active) {
                    onMenuClick.invoke(menuOption)
                }
            },
    )
}

interface MenuOption {
    @get:StringRes
    val title: Int
    var active: Boolean
    var visible: Boolean
}

data class Select(
    override val title: Int = com.skydio.strings.R.string.actionMenu_select,
    override var active: Boolean = true,
    override var visible: Boolean = true,
) : MenuOption

data class Back(
    override val title: Int = com.skydio.strings.R.string.nightsense_back,
    override var active: Boolean = true,
    override var visible: Boolean = true,
) : MenuOption

class Export(
    override val title: Int = com.skydio.strings.R.string.actionMenu_export,
    override var active: Boolean = true,
    override var visible: Boolean = true,
) : MenuOption

class Delete(
    override val title: Int = com.skydio.strings.R.string.actionMenu_delete,
    override var active: Boolean = true,
    override var visible: Boolean = true,
) : MenuOption
