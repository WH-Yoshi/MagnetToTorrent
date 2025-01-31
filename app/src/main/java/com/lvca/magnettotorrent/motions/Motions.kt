package com.lvca.magnettotorrent.motions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

fun materialSharedAxisXIn(
    initialOffsetX: (fullWidth: Int) -> Int,
    durationMillis: Int = 300
): EnterTransition = slideInHorizontally(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = FastOutSlowInEasing
    ),
    initialOffsetX = initialOffsetX
) + fadeIn(
    animationSpec = tween(
        durationMillis = durationMillis,
        delayMillis = durationMillis,
        easing = LinearOutSlowInEasing
    )
)

fun materialSharedAxisXOut(
    targetOffsetX: (fullWidth: Int) -> Int,
    durationMillis: Int = 300
): ExitTransition = slideOutHorizontally(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = FastOutSlowInEasing
    ),
    targetOffsetX = targetOffsetX
) + fadeOut(
    animationSpec = tween(
        durationMillis = durationMillis,
        delayMillis = 0,
        easing = FastOutLinearInEasing
    )
)

fun materialSharedAxisYIn(
    initialOffsetY: (fullHeight: Int) -> Int,
    durationMillis: Int = 300
): EnterTransition = slideInVertically(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = FastOutSlowInEasing
    ),
    initialOffsetY = initialOffsetY
) + fadeIn(
    animationSpec = tween(
        durationMillis = durationMillis,
        delayMillis = 0,
        easing = LinearOutSlowInEasing
    ),
    initialAlpha = 0.05f
)

fun materialSharedAxisYOut(
    targetOffsetY: (fullHeight: Int) -> Int,
    durationMillis: Int = 300
): ExitTransition = slideOutVertically(
    animationSpec = tween(
        durationMillis = durationMillis,
        easing = FastOutSlowInEasing
    ),
    targetOffsetY = targetOffsetY
) + fadeOut(
    animationSpec = tween(
        durationMillis = durationMillis,
        delayMillis = 0,
        easing = FastOutLinearInEasing
    ),
    targetAlpha = 0.05f
)