package com.skydio.ui.components.widget.indicators

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.container.SkydioCard
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget
import com.skydio.ui.designsystem.*

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a progress indicator.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioBatteryIndicator @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioBatteryIndicator.State>(
        context,
        attrs,
        defaultStyleAttr,
        defaultViewState = State()
) {

    /**
     * The entire state of any given [SkydioBatteryIndicator].
     */
    data class State(
            val progressPercent: Float = 0f,
            val detailedProgress: DetailedProgress? = null,
            override var isEnabled: Boolean = true,
            override var isVisible: Boolean = true,
    ) : BaseViewState() {

        data class DetailedProgress(val leftText: String?, val rightText: String?)

        internal val progressPercentageText = "${(100 * progressPercent).toInt()}%"
    }

    @Composable
    override fun Content(state: State) = SkydioBatteryIndicator(state = state)

}

// MARK: Composable Impl

@Composable
fun SkydioBatteryIndicator(
        state: SkydioBatteryIndicatorState,
        modifier: Modifier = Modifier,
        theme: AppTheme = getAppTheme()
) = SkydioBatteryIndicator(
        progressPercent = state.progressPercent,
        leftText = state.detailedProgress?.leftText ?: state.progressPercentageText,
        rightText = state.detailedProgress?.rightText,
        modifier = modifier,
        theme = theme
)

@Composable
fun SkydioBatteryIndicator(
    progressPercent: Float,
    modifier: Modifier = Modifier,
    leftText: String? = null,
    rightText: String? = null,
    theme: AppTheme = getAppTheme(),
    colors: BatteryIndicatorColors = BatteryIndicatorColors.defaultThemeColors(theme),
    detailTextStyle: TextStyle = theme.typography.bodyLarge,
) {
    SkydioCard(theme = theme) {
        Column(modifier = modifier) {
            val lineColor = when {
                progressPercent == 1f -> colors.progressColor
                else -> colors.progressColor
            }

            if (leftText != null || rightText != null)
                Row(modifier = modifier.height(20.dp)) {
                    ProvideTextStyle(value = detailTextStyle.copy(color = colors.detailTextColor)) {
                        if (leftText != null)  Text(text = leftText)
                        Spacer(modifier = Modifier.weight(1f))
                        if (rightText != null) Text(text = rightText)
                    }
                }

            Spacer(modifier = Modifier.height(4.dp))

            Canvas(
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
            ) {

                drawLine(
                        color = colors.progressIncompleteColor,
                        start = Offset(x = 0f, y = size.height / 2),
                        end = Offset(x = size.width, y = size.height / 2),
                        strokeWidth = 6.dp.toPx(),
                )

                drawLine(
                        color = lineColor,
                        start = Offset(x = 0f, y = size.height / 2),
                        end = Offset(x = size.width * progressPercent, y = size.height / 2),
                        strokeWidth = 6.dp.toPx(),
                )
            }
        }
    }
}

// MARK: Utils

typealias SkydioBatteryIndicatorState = SkydioBatteryIndicator.State

typealias SkydioBatteryIndicatorDetail = SkydioBatteryIndicator.State.DetailedProgress

// MARK: Theming

data class BatteryIndicatorColors(
        val detailTextColor: Color,
        val progressColor: Color,
        val progressIncompleteColor: Color,
) {
    companion object {
        fun defaultThemeColors(
                theme: AppTheme,
                detailTextColor: Color = when (theme) {
                    AppTheme.Dark -> Gray0
                    AppTheme.Light -> Gray600
                },
                progressColor: Color = when (theme) {
                    AppTheme.Dark -> Green400
                    AppTheme.Light -> Green500
                },
                progressIncompleteColor: Color = when (theme) {
                    AppTheme.Dark -> Gray800
                    AppTheme.Light -> Gray100
                },
        ) = BatteryIndicatorColors(
                detailTextColor = detailTextColor,
                progressColor = progressColor,
                progressIncompleteColor = progressIncompleteColor,
        )
    }
}
