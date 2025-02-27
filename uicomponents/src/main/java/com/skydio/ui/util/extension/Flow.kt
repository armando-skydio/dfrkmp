package com.skydio.ui.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Helper function for creating a simple, immutable [StateFlow].
 */
fun <T> stateFlowOf(value: T): StateFlow<T> = MutableStateFlow(value)

/**
 * This function intelligently collects a [Flow] as a [State].
 * If the flow this is called on is a [StateFlow], then it will be collected with that state
 * flow's current value as the initial value, otherwise [initial] will be used.
 *
 * This should be the primary way of collecting state in our app, so that preview view models
 * can properly set state statically.
 */
@Composable
fun <T> Flow<T>.smartCollectAsState(initial: T): State<T> =
    if (this is StateFlow) this.collectAsState()
    else this.collectAsState(initial = initial)