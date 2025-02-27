package com.skydio.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter

/**
 * Composable that can be used to totally block touch inputs when overlaid on another view.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TouchSink(modifier: Modifier = Modifier) =
    Box(modifier = modifier.pointerInteropFilter { true })