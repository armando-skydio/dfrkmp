package com.skydio.ui.components.widget.text

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Gray1000
import com.skydio.ui.designsystem.SkydioTypography
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ThemedPreviews

/**
 * Text wrapper to automatically handle changes is theme.
 *
 * @param text Text to show
 * @param style [SkydioTypography] to be applied to the text
 * @param modifier the [Modifier] to be applied to this layout node
 * @param theme the app's currently-selected theme
 * @param color color to override the style with
 */
@Composable
fun SkydioText(
    text: CharSequence,
    style: TextStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    dropShadow: Boolean = false,
    stroke: Boolean = false,
    theme: AppTheme = getAppTheme(),
    maxLines: Int = Int.MAX_VALUE,
    color: Color = style.color,
) {
    val adjustedStyle = style.let {
        if (!dropShadow) it
        else it.copy(
            shadow = Shadow(
                color = theme.colors.appBackgroundColor.copy(alpha = 0.5f),
                offset = Offset(x = 2f, y = 2f),
                blurRadius = 20f
            ))
    }
    ProvideTextStyle(value = adjustedStyle) {
        if (stroke) {
            Box {
                if (text is AnnotatedString) {
                    Text(
                        modifier = modifier,
                        text = text,
                        color = color,
                        textAlign = textAlign,
                        overflow = overflow,
                        maxLines = maxLines,
                    )
                    Text(
                        modifier = modifier
                            .zIndex(1f),
                        text = text,
                        style = style.copy(
                            color = Gray1000,
                            drawStyle = Stroke(
                                width = 1.5f,
                                miter = 10f,
                                join = StrokeJoin.Round
                            )
                        ),
                        textAlign = textAlign,
                        overflow = overflow,
                        maxLines = maxLines,
                    )
                } else {
                    Text(
                        modifier = modifier,
                        text = text.toString(),
                        color = color,
                        textAlign = textAlign,
                        overflow = overflow,
                        maxLines = maxLines,
                    )
                    Text(
                        modifier = modifier
                            .zIndex(1f),
                        text = text.toString(),
                        style = style.copy(
                            color = Gray1000,
                            drawStyle = Stroke(
                                width = 1.5f,
                                miter = 10f,
                                join = StrokeJoin.Round
                            )
                        ),
                        textAlign = textAlign,
                        overflow = overflow,
                        maxLines = maxLines,
                    )
                }
            }
        } else {
            if (text is AnnotatedString) {
                Text(
                    modifier = modifier,
                    text = text,
                    color = color,
                    textAlign = textAlign,
                    overflow = overflow,
                    maxLines = maxLines,
                )
            } else {
                Text(
                    modifier = modifier,
                    text = text.toString(),
                    color = color,
                    textAlign = textAlign,
                    overflow = overflow,
                    maxLines = maxLines,
                )
            }
        }
    }
}

@Preview
@Composable
fun TextSkydioText() {
    ThemedPreviews { theme ->
        SkydioText(text = "Hello World!", style = theme.typography.bodyLarge)
        SkydioText(text = "Hello World!", dropShadow = true, style = theme.typography.bodyLarge)
    }
}
