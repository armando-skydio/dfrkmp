package com.skydio.ui

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset
import kotlin.math.abs

enum class AnimationEnterDirection(internal val initialOffsetSign: Int) {
    FROM_RIGHT(1),
    FROM_LEFT(-1),
    FROM_BOTTOM(1),
    FROM_TOP(-1)
}

enum class AnimationExitDirection(internal val targetOffsetSign: Int) {
    TO_RIGHT(1),
    TO_LEFT(-1),
    TO_BOTTOM(1),
    TO_TOP(-1)
}

fun enterHorizontally(
    direction: AnimationEnterDirection,
    customAnimationSpec: FiniteAnimationSpec<IntOffset>? = null,
    targetOffsetSize: (fullWidth: Int) -> Int = { (it / 2) }
) = slideInHorizontally(
    animationSpec = customAnimationSpec ?: tween(),
    initialOffsetX = { abs(targetOffsetSize(it)) * direction.initialOffsetSign }
)

fun enterVertically(
    direction: AnimationEnterDirection,
    customAnimationSpec: FiniteAnimationSpec<IntOffset>? = null,
    targetOffsetSize: (fullHeight: Int) -> Int = { (it / 2) }
) = slideInVertically(
    animationSpec = customAnimationSpec ?: tween(),
    initialOffsetY = { abs(targetOffsetSize(it)) * direction.initialOffsetSign }
)

fun exitHorizontally(
    direction: AnimationExitDirection,
    customAnimationSpec: FiniteAnimationSpec<IntOffset>? = null,
    targetOffsetSize: (fullWidth: Int) -> Int = { (it / 2) }
) = slideOutHorizontally(
    animationSpec = customAnimationSpec ?: tween(),
    targetOffsetX = { abs(targetOffsetSize(it)) * direction.targetOffsetSign }
)

fun exitVertically(
    direction: AnimationExitDirection,
    customAnimationSpec: FiniteAnimationSpec<IntOffset>? = null,
    targetOffsetSize: (fullHeight: Int) -> Int = { (it / 2) }
) = slideOutVertically(
    animationSpec = customAnimationSpec ?: tween(),
    targetOffsetY = { abs(targetOffsetSize(it)) * direction.targetOffsetSign }
)
