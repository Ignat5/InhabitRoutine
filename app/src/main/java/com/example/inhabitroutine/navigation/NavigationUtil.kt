package com.example.inhabitroutine.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

/* TOP DESTINATION */
private const val topDestinationEnterExitDurationMillis = 150
private const val topDestinationEnterExitDelayMillis = 150
private val topDestinationEnterExitEasing = CubicBezierEasing(0f, 0f, 0f, 1f)

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
private val enterScreenEasing: Easing = CubicBezierEasing(0f, 0.2f, 0.5f, 1f)

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
private const val popScreenDurationMillis = 200
private const val popScreenDelayMillis = 50
private val popScreenEasing: Easing = CubicBezierEasing(0.3f, 0f, 1f, 1f)

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