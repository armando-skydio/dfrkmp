package com.skydio.ui.components.container

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier

@Suppress("NAME_SHADOWING")
@Composable
fun <T> SkydioFixedGrid(
    modifier: Modifier = Modifier,
    items: List<T>,
    columns: Int,
    allowLastRowToFill: Boolean = false,
    itemContent: @Composable BoxScope.(index: Int, item: T) -> Unit
) {
    val columns by rememberUpdatedState(columns)
    val items by rememberUpdatedState(items)
    val rows by remember { derivedStateOf { items.chunked(columns) } }

    var indexTracker = 0
    Column(modifier = modifier) {
        rows.forEachIndexed { index, row ->
            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                row.forEach { item ->
                    val index = indexTracker
                    Box(modifier = Modifier.weight(1f)) { itemContent(index, item) }
                    indexTracker++
                }

                // Maybe add extra padding to the row
                if (index == rows.size - 1 && !allowLastRowToFill) {
                    val neededPadding = columns - row.size
                    for (i in 0 until neededPadding) Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
