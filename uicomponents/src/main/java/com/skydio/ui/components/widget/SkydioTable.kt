package com.skydio.ui.components.widget

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.core.Image
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Gray800
import com.skydio.ui.designsystem.getAppTheme
import java.util.UUID

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a table list.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioTable @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioTable.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    var rowClickListener: RowClickListener? = null

    data class RowState(
        val title: String? = null,
        val subtitle: String? = null,
        val label: String? = null,
        val iconLeft: Int? = null,
        val iconRight: Int? = null,
        val rowId: String = UUID.randomUUID().toString(),
    )

    /**
     * The entire state of any given [SkydioTable].
     */
    data class State(
        val rows: List<RowState> = emptyList(),
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true,
    ) : BaseViewState()

    /**
     * Functional callback interface for item clicks.
     */
    fun interface RowClickListener {
        fun onRowClick(index: Int, id: String)
    }

    @Composable
    override fun Content(state: State) = SkydioTable(
        state = state,
        onRowClicked = { index: Int, id: String -> rowClickListener?.onRowClick(index, id) }
    )

}

// MARK: Composable Impl

@Composable
fun SkydioTable(
    state: TableState,
    onRowClicked: ((Int, String) -> Unit)? = null,
    theme: AppTheme = getAppTheme(),
    colors: TableColors = TableColors.defaultThemeColors(theme),
) = SkydioTable(
    rows = state.rows,
    onRowClicked = { index: Int, id: String -> onRowClicked?.invoke(index, id) },
    theme = theme,
    colors = colors,
)

@Composable
fun SkydioTable(
    modifier: Modifier = Modifier,
    // list
    rows: List<RowState> = emptyList(),
    onRowClicked: ((index: Int, id: String) -> Unit)? = null,
    // styling
    theme: AppTheme = getAppTheme(),
    shape: Shape = theme.shapes.mediumRoundedCorners,
    colors: TableColors = TableColors.defaultThemeColors(theme),
) {
    Column(
        modifier = modifier
            .clip(shape)
            .border(
                width = 1.dp,
                color = colors.outlineColor,
                shape = shape),
    ) {
        rows.forEachIndexed { index, rowState ->
            SkydioTableRow(
                state = rowState,
                onClicked = { onRowClicked?.invoke(index, rowState.rowId) },
                theme = theme,
                colors = colors)
            if (index < rows.size - 1)
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = colors.outlineColor)
        }
    }
}

@Composable
fun SkydioTableRow(
    state: RowState,
    modifier: Modifier = Modifier,
    onClicked: (() -> Unit)? = null,
    theme: AppTheme = getAppTheme(),
    colors: TableColors = TableColors.defaultThemeColors(theme),
) = SkydioTableRow(
    title = state.title.orEmpty(),
    subtitle = state.subtitle,
    label = state.label,
    iconLeft = state.iconLeft?.let { DrawableImageSource(it, state.label) },
    iconRight = state.iconRight?.let { DrawableImageSource(it, state.label) },
    onClicked = onClicked,
    modifier = modifier,
    theme = theme,
    colors = colors
)

@Composable
fun SkydioTableRow(
    modifier: Modifier = Modifier,
    title: String = "",
    subtitle: String? = null,
    label: String? = null,
    iconLeft: ImageSource? = null,
    iconRight: ImageSource? = null,
    onClicked: (() -> Unit)? = null,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    colors: TableColors = TableColors.defaultThemeColors(theme),
) {
    Row(
        modifier = modifier
            .background(color = colors.rowBackgroundColor)
            .padding(12.dp)
            .clickable {
                onClicked?.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconLeft?.let {
            Image(
                modifier = Modifier.size(24.dp),
                source = iconLeft,
                colorFilter = ColorFilter.tint(color = colors.rowIconTint))
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column {
            ProvideTextStyle(value = textStyle.copy(color = colors.rowPrimaryTextColor)) {
                Text(text = title)
                if (subtitle != null)
                    Text(
                        text = subtitle,
                        style = textStyle.copy(color = colors.rowSecondaryTextColor))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        ProvideTextStyle(value = theme.typography.bodyLarge) {
            if (label != null) Text(text = label)
        }

        iconRight?.let {
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                modifier = Modifier.size(24.dp),
                source = iconRight,
                colorFilter = ColorFilter.tint(color = colors.rowActionTint))
        }
    }
}

// MARK: Utils

typealias TableState = SkydioTable.State

typealias RowState = SkydioTable.RowState

// MARK : Theming

data class TableColors(
    val rowBackgroundColor: Color,
    val rowPrimaryTextColor: Color,
    val rowSecondaryTextColor: Color,
    val rowIconTint: Color,
    val rowActionTint: Color,
    val outlineColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            rowBackgroundColor: Color = theme.colors.defaultContainerBackgroundColor,
            rowPrimaryTextColor: Color = theme.colors.primaryTextColor,
            rowSecondaryTextColor: Color = theme.colors.secondaryTextColor,
            rowIconTint: Color = theme.colors.primaryTextColor,
            rowActionTint: Color = theme.colors.primaryTextColor,
            outlineColor: Color = Gray800,
        ) = TableColors(
            rowBackgroundColor = rowBackgroundColor,
            rowPrimaryTextColor = rowPrimaryTextColor,
            rowSecondaryTextColor = rowSecondaryTextColor,
            rowIconTint = rowIconTint,
            rowActionTint = rowActionTint,
            outlineColor = outlineColor,
        )
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun TablePreview() = ThemedPreviews { theme -> TableExample(theme) }

@Composable
fun TableExample(theme: AppTheme = AppTheme.Dark) {
    val sampleRow = RowState(
        title = "Title",
        subtitle = "Subtitle",
        label = "25%",
        iconLeft = com.skydio.ui.components.R.drawable.ic_placeholder_icon,
        iconRight = com.skydio.ui.components.R.drawable.ic_chevron_right)
    SkydioTable(
        rows = listOf(sampleRow, sampleRow, sampleRow),
        theme = theme)
}
