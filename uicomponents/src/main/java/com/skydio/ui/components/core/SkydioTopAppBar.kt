package com.skydio.ui.components.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme

val appBarHorizontalPadding = 4.dp
val titleIconModifier = Modifier
    .fillMaxHeight()
    .width(72.dp - appBarHorizontalPadding)

@Composable
fun SkydioTopAppBar(
    title: String,
    theme: AppTheme = getAppTheme(),
    onBack: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth()) {
        // TopAppBar Content
        Box(Modifier.height(32.dp)) {
            // Navigation Icon
            Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high
                ) {
                    IconButton(
                        onClick = onBack,
                        enabled = true
                    ) {
                    }
                }
            }
            // NOTE(armando) - Custom toolbar to align text to center
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProvideTextStyle(value = theme.typography.headlineMedium) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            text = title.uppercase()
                        )
                    }
                }
            }
        }
    }
}
