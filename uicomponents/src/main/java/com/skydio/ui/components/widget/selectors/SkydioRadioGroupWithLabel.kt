package com.skydio.ui.components.widget.selectors

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ThemedPreviews

@Composable
fun SkydioRadioGroupWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    detail: String? = null,
    state: RadioGroupState,
    rowModifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    theme: AppTheme = getAppTheme(),
    onSelectionChanged: (selectedIndex: Int) -> Unit
) {
    Column {
        if (label.isNotBlank())
            SkydioText(text = label, style = theme.typography.headlineMedium)
        SkydioRadioGroup(
            selectedIndex = state.value,
            options = state.options,
            enabled = state.isEnabled,
            modifier = modifier,
            rowModifier = rowModifier,
            theme = theme,
            onSelectionChanged = {
                state.value = it
                onSelectionChanged(it)
            })

        if (detail.isNullOrBlank().not())
            SkydioText(
                text = detail.orEmpty(),
                style = theme.typography.bodyLarge,
                color = theme.colors.secondaryTextColor)
    }
}

@Preview
@Composable
fun testSkydioRadioGroupWithLabel() {
    ThemedPreviews { theme ->
        SkydioRadioGroupWithLabel(
            label = "Hello World!",
            detail = "How are you?",
            state = RadioGroupState(options = listOf("Test1", "Test2", "Test3")),
            theme = theme,
            onSelectionChanged = {})
    }
}
