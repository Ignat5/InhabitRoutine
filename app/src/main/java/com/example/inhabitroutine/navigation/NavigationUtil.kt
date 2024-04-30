package com.example.inhabitroutine.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavBackStackEntry

/* TOP DESTINATION */
private const val topDestinationEnterExitDurationMillis = 1000
private const val topDestinationEnterExitDelayMillis = 200
private val topDestinationEnterExitEasing = FastOutSlowInEasing

fun AnimatedContentTransitionScope<NavBackStackEntry>.topDestinationEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = topDestinationEnterExitDurationMillis,
            delayMillis = topDestinationEnterExitDelayMillis,
            easing = topDestinationEnterExitEasing
        )
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.topDestinationExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = topDestinationEnterExitDurationMillis,
            delayMillis = topDestinationEnterExitDelayMillis,
            easing = topDestinationEnterExitEasing
        )
    )
}

/* FORWARD/BACKWARD */

/* ENTER/EXIT */
private const val enterScreenDurationMillis = 400
private const val enterScreenDelayMillis = 100
private val enterScreenEasing: Easing = FastOutSlowInEasing //CubicBezierEasing(0f, 0.2f, 0.5f, 1f)

fun AnimatedContentTransitionScope<NavBackStackEntry>.forwardEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = tween(
            durationMillis = enterScreenDurationMillis,
            delayMillis = enterScreenDelayMillis,
            easing = enterScreenEasing
        )
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = enterScreenDurationMillis,
            delayMillis = enterScreenDelayMillis,
            easing = enterScreenEasing
        )
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.backwardExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = tween(
            durationMillis = enterScreenDurationMillis,
            delayMillis = enterScreenDelayMillis,
            easing = enterScreenEasing
        )
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = enterScreenDurationMillis,
            delayMillis = enterScreenDelayMillis,
            easing = enterScreenEasing
        )
    )
}

/* POP */
private const val popScreenDurationMillis = enterScreenDurationMillis / 2
private const val popScreenDelayMillis = enterScreenDelayMillis / 2
private val popScreenEasing: Easing = FastOutSlowInEasing //CubicBezierEasing(0.3f, 0f, 1f, 1f)

fun AnimatedContentTransitionScope<NavBackStackEntry>.forwardPopEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = tween(
            durationMillis = popScreenDurationMillis,
            delayMillis = popScreenDelayMillis,
            easing = popScreenEasing
        )
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = popScreenDurationMillis,
            delayMillis = popScreenDelayMillis,
            easing = popScreenEasing
        )
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.backwardPopExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = tween(
            durationMillis = popScreenDurationMillis,
            delayMillis = popScreenDelayMillis,
            easing = popScreenEasing
        )
    ) + fadeOut(
        animationSpec = tween(
            durationMillis = popScreenDurationMillis,
            delayMillis = popScreenDelayMillis,
            easing = popScreenEasing
        )
    )
}