package com.skydio.ui.components.widget.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.skydio.ui.components.R
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource

/**
 * Wrapper around basic skydio text inputs designed specially for search inputs.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SkydioSearchInput(
    // core state
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isFocused: Boolean = false,
    // decoration
    errorMessage: String? = null,
    allowClearButton: Boolean = true,
    showLoadingIndicator: Boolean = false,
    // styling
    theme: AppTheme = getAppTheme(),
    colors: TextInputColors = TextInputColors.defaultThemeColors(theme),
    inputTextStyle: TextStyle = theme.typography.bodyLarge,
    // callbacks
    onTextChanged: (TextFieldValue) -> Unit = {},
    onSearchClicked: (String) -> Boolean = { true }
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val showClearButton = value.text.isNotBlank() && allowClearButton
    val trailingIcon = if (showClearButton) DrawableImageSource(R.drawable.ic_close) else null

    fun handleOnSearchClicked() {
        if (onSearchClicked(value.text)) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    SkydioTextInput(
        modifier = modifier,
        value = value,
        placeholder = "",
        errorText = errorMessage,
        showLoadingIndicator = showLoadingIndicator,
        isEnabled = isEnabled,
        isFocused = isFocused,
        trailingIcon = trailingIcon,
        onTrailingIconClicked = { onTextChanged(TextFieldValue()) },
        theme = theme,
        colors = colors,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { handleOnSearchClicked() }),
        inputTextStyle = inputTextStyle,
        onTextChanged = { if (it != value) onTextChanged(it) }
    )
}

/**
 * Search input field with UI that supports autofill suggestions.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> SkydioAutofillSearchInput(
    modifier: Modifier = Modifier,
    // core state
    value: TextFieldValue,
    suggestions: List<T> = emptyList(),
    suggestionContent: @Composable (T) -> Unit,
    isEnabled: Boolean = true,
    isFocused: Boolean = false,
    // decoration
    errorMessage: String? = null,
    allowClearButton: Boolean = true,
    showLoadingIndicator: Boolean = false,
    // styling
    theme: AppTheme = getAppTheme(),
    colors: TextInputColors = TextInputColors.defaultThemeColors(theme),
    inputTextStyle: TextStyle = theme.typography.bodyLarge,
    // callbacks
    onTextChanged: (TextFieldValue) -> Unit = {},
    onSearchClicked: (String) -> Boolean = { true },
    onSuggestionClicked: (T) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val textValue by rememberUpdatedState(value)

    fun onClickSuggestion(suggestion: T) {
        focusManager.clearFocus()
        keyboardController?.hide()
        onSuggestionClicked(suggestion)
    }

    Column(modifier = modifier) {
        SkydioSearchInput(
            value = textValue,
            isEnabled = isEnabled,
            isFocused = isFocused,
            errorMessage = if (suggestions.isEmpty()) errorMessage else null,
            allowClearButton = allowClearButton,
            showLoadingIndicator = showLoadingIndicator,
            theme = theme,
            colors = colors,
            inputTextStyle = inputTextStyle,
            onTextChanged = onTextChanged,
            onSearchClicked = onSearchClicked,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
        )

        val suggestionsList by rememberUpdatedState(suggestions)
        if (suggestionsList.isNotEmpty())
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .zIndex(0f)
                    .fillMaxWidth()
                    .background(color = colors.backgroundColor)
            ) {
                suggestionsList.forEach { suggestion ->
                    ProvideTextStyle(value = inputTextStyle) {
                        Box(modifier = Modifier.clickable { onClickSuggestion(suggestion) }) {
                            suggestionContent(suggestion)
                        }
                    }
                }
            }
    }
}
