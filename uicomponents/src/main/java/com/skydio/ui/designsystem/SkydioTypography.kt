package com.skydio.ui.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.skydio.ui.components.R
/**
 * Class (with defaults) for defining UI font styles which can, through the use of extension
 * functions, define theme-specific typography. By default, font colors will be inherited from
 * the [primaryTextColor] of the [parentTheme].
 */
data class SkydioTypography(
    val parentTheme: AppTheme,
    val blenderSmall: TextStyle = TextStyle(
        fontSize = 14.0.sp,
        fontFamily = blender,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(500.0.toInt()),
        color = parentTheme.colors.primaryTextColor,
    ),
    val telemetryBlender: TextStyle = TextStyle(
        fontSize = 15.0.sp,
        fontFamily = blender,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(500.0.toInt()),
        color = parentTheme.colors.primaryTextColor,
    ),
    val blenderMedium: TextStyle = TextStyle(
        fontSize = 18.0.sp,
        fontFamily = blender,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(500.0.toInt()),
        color = parentTheme.colors.primaryTextColor,
    ),
    val blenderLarge: TextStyle = TextStyle(
        fontSize = 18.0.sp,
        fontFamily = blender,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight(700.0.toInt()),
        color = parentTheme.colors.primaryTextColor,
    ),
    val headlineLarge: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 23.0.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.primaryTextColor,
    ),
    val headlineMedium: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 17.0.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.primaryTextColor,
    ),
    val headlineSmall: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 15.0.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.primaryTextColor,
    ),
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 15.0.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.primaryTextColor,
    ),
    val body: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 13.0.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.primaryTextColor,
    ),
    val bodyBold: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 13.0.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.primaryTextColor,
    ),
    val footnote: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 11.0.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.primaryTextColor,
    ),
    val footnoteSecondary: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 11.0.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.secondaryTextColor,
    ),
    val footnoteNormal: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 11.0.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Left,
        color = parentTheme.colors.primaryTextColor,
    ),
    val link: TextStyle = TextStyle(
        fontFamily = case,
        fontSize = 13.0.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Left,
        color = Blue500,
    )
) {

    companion object {
        val blender = FontFamily(
            Font(R.font.blender_bold, FontWeight.Normal),
            Font(R.font.blender_medium, FontWeight.Medium),
        )

        val case = FontFamily(
            Font(R.font.case_font, FontWeight.Normal),
            Font(R.font.case_medium_normal, FontWeight.Medium),
            Font(R.font.case_semibold_normal, FontWeight.SemiBold)
        )

        fun TextStyle.withShadow(): TextStyle = this.copy(
            shadow = Shadow(
                color = Gray1000,
                offset = Offset(x = 0.5f, y = 2f),
                blurRadius = 0f
            ),
        )

    }

}

@Preview(widthDp = 315, heightDp = 315, showBackground = true)
@Composable
private fun TypographyPreview(theme: AppTheme = getAppTheme()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(modifier = Modifier.background(color = theme.colors.defaultContainerBackgroundColor)) {
            ProvideTextStyle(value = theme.typography.headlineLarge) {
                Text(text = "headlineLarge")
            }
            ProvideTextStyle(value = theme.typography.headlineMedium) {
                Text(text = "headlineMedium")
            }
            ProvideTextStyle(value = theme.typography.headlineSmall) {
                Text(text = "headlineSmall")
            }
            ProvideTextStyle(value = theme.typography.bodyLarge) {
                Text(text = "bodyLarge")
            }
            ProvideTextStyle(value = theme.typography.body) {
                Text(text = "body")
            }
            ProvideTextStyle(value = theme.typography.footnote) {
                Text(text = "footnote")
            }
            ProvideTextStyle(value = theme.typography.blenderLarge) {
                Text(text = "blenderLarge")
            }
            ProvideTextStyle(value = theme.typography.blenderMedium) {
                Text(text = "blenderMedium")
            }
            ProvideTextStyle(value = theme.typography.blenderSmall) {
                Text(text = "blenderSmall")
            }
        }
    }
}
