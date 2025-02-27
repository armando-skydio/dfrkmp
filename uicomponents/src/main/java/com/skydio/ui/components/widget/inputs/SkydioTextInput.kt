package com.skydio.ui.components.widget.inputs

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Image
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget
import com.skydio.ui.components.widget.indicators.SkydioLoadingIndicator
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.*
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.extension.noRippleClickableIf

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a text input field.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioTextInput.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    var onTextChangedListener: OnTextChangedListener? = null

    /**
     * The entire state of any given [SkydioTextInput].
     */
    data class State(
        override var value: String? = null,
        val placeholder: String? = null,
        val header: String? = null,
        val errorText: String? = null,
        val isRequired: Boolean = false,
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true
    ) : UserEditableViewState<String?>()

    /**
     * Functional callback interface for text changes.
     */
    fun interface OnTextChangedListener {
        fun onTextChanged(text: String)
    }

    @Composable
    override fun Content(state: State) = SkydioTextInput(
        state = state,
        onTextChanged = { onTextChangedListener?.onTextChanged(it) }
    )

}

// MARK: Composable Impl

@Composable
@Deprecated(message = "Only use for legacy XML views")
fun SkydioTextInput(
    state: TextInputState,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    onTextChanged: (String) -> Unit = {},
) {
    val kbType = KeyboardType.Text
    SkydioTextInput(
        value = TextFieldValue(state.value.orEmpty()),
        header = state.header,
        placeholder = state.placeholder,
        errorText = state.errorText,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = kbType),
        isRequired = state.isRequired,
        isEnabled = state.isEnabled,
        modifier = modifier,
        theme = theme,
        onTextChanged = {
            state.value = it.text
            onTextChanged(it.text)
        },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SkydioTextInput(
    // core state
    value: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isFocused: Boolean = false,
    // decorationit.text
    header: String? = null,
    placeholder: String? = null,
    errorText: String? = null,
    showLoadingIndicator: Boolean = false,
    leadingIcon: ImageSource? = null,
    onLeadingIconClicked: (() -> Unit)? = null,
    trailingIcon: ImageSource? = null,
    onTrailingIconClicked: (() -> Unit)? = null,
    isRequired: Boolean = false,    // show *
    isPassword: Boolean = false,    // KeyboardType.Password and show/hide password eye icon
    maxCharacters: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    // input type
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    // styling
    theme: AppTheme = getAppTheme(),
    colors: TextInputColors = TextInputColors.defaultThemeColors(theme),
    inputTextStyle: TextStyle = theme.typography.bodyLarge,
    errorTextStyle: TextStyle = theme.typography.bodyBold.copy(color = colors.errorOutlineColor),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    onlyDigits: Boolean = false
) {

    // states and colors

    val errorTextState = rememberUpdatedState(errorText)

    val isFocusedState = remember { mutableStateOf(isFocused) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(isFocused) { if (isFocused) focusRequester.requestFocus() }

    // not using remember(value), it looses the selection, see SW-65527
    var internalValue by remember { mutableStateOf(value) }
    LaunchedEffect(value) {
        if (!internalValue.text.equals(value.text)) {
            internalValue = value
        }
    }

    val backgroundColor = when {
        !isEnabled -> colors.disabledBackgroundColor
        !errorTextState.value.isNullOrBlank() -> colors.errorBackgroundColor
        isFocusedState.value -> colors.focusedBackgroundColor
        else -> colors.backgroundColor
    }
    val outlineColor = when {
        !isEnabled -> colors.disabledOutlineColor
        !errorTextState.value.isNullOrBlank() -> colors.errorOutlineColor
        isFocusedState.value -> colors.focusedOutlineColor
        else -> Gray400
    }
    val textInputColor = when {
        !isEnabled -> colors.disabledTextColor
        else -> colors.textColor
    }
    // views

    val textStyle = inputTextStyle.copy(color = textInputColor)

    Column(modifier = modifier) {
        ProvideTextStyle(value = textStyle) {

            // header/label text

            if (!header.isNullOrBlank())
                Text(text = header, modifier = Modifier.padding(bottom = 8.dp))

            // input field

            val interactionSource = remember { MutableInteractionSource() }

            var showPassword by remember { mutableStateOf(!isPassword) }
            val visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation()

            val trailing: @Composable (() -> Unit)? =
                if (trailingIcon != null || isPassword || isRequired) {
                    {
                        Row {
                            if (isRequired)
                                Text(text = "*", color = textInputColor)
                            if (trailingIcon != null) {
                                if (isRequired) Spacer(modifier = Modifier.width(4.dp))
                                Image(source = trailingIcon, modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .padding(8.dp)
                                    .noRippleClickableIf(onTrailingIconClicked != null) {
                                        onTrailingIconClicked?.invoke()
                                    }
                                )
                            }
                            if (isPassword) {
                                Image(source = ImageSource.Resource(
                                    if (showPassword) R.drawable.ic_eye_slash else R.drawable.ic_eye,
                                    ""
                                ), modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .padding(8.dp)
                                    .clickable {
                                        showPassword = !showPassword
                                    }
                                )
                            }
                        }
                    }
                } else null

            val leading: @Composable (() -> Unit)? =
                if (leadingIcon != null || showLoadingIndicator) {
                    {
                        if (showLoadingIndicator) {
                            SkydioLoadingIndicator(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 6.dp)
                            )
                        } else if (leadingIcon != null)
                            Image(source = leadingIcon, modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                                .padding(8.dp)
                                .noRippleClickableIf(onLeadingIconClicked != null) {
                                    onLeadingIconClicked?.invoke()
                                }
                            )
                    }
                } else null

            BasicTextField(
                value = internalValue,
                onValueChange = {
                    if (it != internalValue && it.text.length <= maxCharacters) {
                        internalValue = if (onlyDigits) {
                            val formattedText = it.text.replace("\\D".toRegex(), "")
                            TextFieldValue(
                                formattedText,
                                selection = TextRange(formattedText.length))
                        } else {
                            it
                        }
                        onTextChanged(internalValue)
                    }
                },
                singleLine = singleLine,
                enabled = isEnabled,
                textStyle = textStyle,
                cursorBrush = SolidColor(textInputColor),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                visualTransformation = visualTransformation,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .height(32.dp)
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, outlineColor), shape)
                    .background(color = backgroundColor, shape = shape)
                    .onFocusChanged { isFocusedState.value = it.isFocused }
            ) {
                TextFieldDefaults.TextFieldDecorationBox(
                    value = internalValue.text,
                    innerTextField = it,
                    singleLine = true,
                    enabled = true,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    placeholder = {
                        val placeholderColor =
                            if (isEnabled) colors.placeholderTextColor
                            else colors.disabledPlaceholderTextColor
                        if (placeholder.orEmpty().isNotBlank()) Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            text = placeholder.orEmpty(),
                            color = placeholderColor
                        )
                    },
                    trailingIcon = trailing,
                    leadingIcon = leading,
                    contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                        top = 0.dp, bottom = 0.dp
                    ),
                )
            }

            // error text
            if (!errorTextState.value.isNullOrBlank()) SkydioText(
                text = errorTextState.value.orEmpty(),
                style = errorTextStyle,
            )
        }
    }
}

// MARK: Utils

typealias TextInputState = SkydioTextInput.State

typealias TextChangedListener = SkydioTextInput.OnTextChangedListener

// MARK: Theming

data class TextInputColors(
    val textColor: Color,
    val disabledTextColor: Color,
    val placeholderTextColor: Color,
    val disabledPlaceholderTextColor: Color,
    val backgroundColor: Color,
    val disabledBackgroundColor: Color,
    val errorBackgroundColor: Color,
    val focusedBackgroundColor: Color,
    val outlineColor: Color,
    val disabledOutlineColor: Color,
    val errorOutlineColor: Color,
    val focusedOutlineColor: Color,
) {
    companion object {
        // TODO(troy): Design team has not specified disabled colors for text input Light theme
        fun defaultThemeColors(
            theme: AppTheme,
            textColor: Color = theme.colors.primaryTextColor,
            disabledTextColor: Color = theme.colors.disabledTextColor,
            placeholderTextColor: Color = theme.colors.secondaryTextColor,
            disabledPlaceholderTextColor: Color = theme.colors.disabledTextColor,
            backgroundColor: Color = theme.colors.defaultWidgetBackgroundColor,
            errorBackgroundColor: Color = when (theme) {
                AppTheme.Dark -> Red900
                AppTheme.Light -> Red100
            },
            disabledBackgroundColor: Color = Gray800,
            focusedBackgroundColor: Color = backgroundColor,
            outlineColor: Color = Transparent,
            disabledOutlineColor: Color = Gray700,
            errorOutlineColor: Color = when (theme) {
                AppTheme.Dark,
                AppTheme.Light -> Red400
            },
            focusedOutlineColor: Color = when (theme) {
                AppTheme.Dark,
                AppTheme.Light -> Blue500
            },
        ) = TextInputColors(
            textColor = textColor,
            disabledTextColor = disabledTextColor,
            placeholderTextColor = placeholderTextColor,
            disabledPlaceholderTextColor = disabledPlaceholderTextColor,
            backgroundColor = backgroundColor,
            disabledBackgroundColor = disabledBackgroundColor,
            errorBackgroundColor = errorBackgroundColor,
            focusedBackgroundColor = focusedBackgroundColor,
            outlineColor = outlineColor,
            disabledOutlineColor = disabledOutlineColor,
            errorOutlineColor = errorOutlineColor,
            focusedOutlineColor = focusedOutlineColor,
        )
    }
}
