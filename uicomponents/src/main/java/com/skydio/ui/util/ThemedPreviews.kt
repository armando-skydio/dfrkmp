package com.skydio.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.AppTheme

/**
 * Composable that calls [content] for each different [AppTheme].
 * Useful for detailed UI previews in Android Studio.
 */
@Composable
fun ThemedPreviews(content: @Composable (AppTheme) -> Unit) {
    val allThemes = listOf(AppTheme.Dark, AppTheme.Light)
    Column {
        allThemes.forEach { theme ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(color = theme.colors.appBackgroundColor)
                    .padding(16.dp)
            ) { content(theme) }
        }
    }
}
