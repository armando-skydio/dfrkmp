package com.skydio.ui.components.widget.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Gray600
import com.skydio.ui.designsystem.Gray700
import com.skydio.ui.designsystem.getAppTheme

// TODO(troy): This should be just about the only text input needed - it supports all configurations
@Composable
fun SkydioTextFieldWithEdgeText(
    value: String,
    modifier: Modifier = Modifier,
    leadingText: String? = null,
    trailingText: String? = null,
    enabled: Boolean = true,
    maxChars: Int = Int.MAX_VALUE,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    imeAction: ImeAction = ImeAction.Done,
    theme: AppTheme = getAppTheme(),
    edgeTextColor: Color = theme.colors.secondaryTextColor,
    onValueChange: (String) -> Unit = {},
    onValueChangeFinished: (String) -> Unit = {},
    validateFunc: (String) -> String = { it },
) {
    val focusManager = LocalFocusManager.current
    val externalValue by rememberUpdatedState(value)
    var internalValue by remember { mutableStateOf(value) }

    val locale = LocalContext.current.resources.configuration.locales[0]

    // Decide the decimal separator based on the locale
    val decimalSeparator = if (locale.language == "es") "," else "."

    LaunchedEffect(key1 = externalValue) {
        internalValue = externalValue
    }

    fun updateValue(value: String) {
        if (value.length <= maxChars) {
            // Replace comma with period internally for proper number handling
            internalValue = value.replace(",", ".")
            onValueChange(internalValue)
        }
    }

    val borderShape = RoundedCornerShape(size = 2.dp)
    val shadow = 4.dp
    Box(
        modifier = modifier
            .shadow(
                elevation = shadow,
                shape = borderShape,
                ambientColor = Color.Transparent,
            )
            .background(Color.Transparent)
            .padding(shadow)
        ,
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Gray600,
                    shape = borderShape
                )
                .height(32.dp)
                .background(color = Gray700, shape = borderShape)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            leadingText?.let {
                Text(
                    text = leadingText,
                    color = edgeTextColor,
                    style = theme.typography.bodyBold,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f))
            }

            // Replace the period with a comma if the locale uses commas
            val transformedText = internalValue .replace(".", decimalSeparator)

            BasicTextField(
                value = transformedText,
                enabled = enabled,
                onValueChange = ::updateValue,
                textStyle = theme.typography.bodyBold,
                maxLines = 1,
                modifier = Modifier.run {
                    if (leadingText == null) weight(1f)
                    else width(IntrinsicSize.Min)
                },
                keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
                visualTransformation = decimalVisualTransformation(decimalSeparator)
            )

            trailingText?.let {
                Text(
                    text = trailingText,
                    color = edgeTextColor,
                    style = theme.typography.bodyBold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

// Custom VisualTransformation to display decimal separator properly based on locale
fun decimalVisualTransformation(decimalSeparator: String): VisualTransformation {
    return VisualTransformation { text ->
        val transformedText = text.text.replace(".", decimalSeparator)
        TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = OffsetMapping.Identity
        )
    }
}

@Composable
@Preview
fun TrailingTextTextFieldPreview() {
    SkydioTextFieldWithEdgeText(
        value = "90",
        leadingText = "Input Percentage",
        trailingText = "%",
        validateFunc = { it },
    )
}
