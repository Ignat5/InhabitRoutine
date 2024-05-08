package com.ignatlegostaev.inhabitroutine.navigation

import androidx.navigation.NavOptions

sealed interface TargetNavDest {
    data class Destination(
        val route: String,
        val navOptions: NavOptions? = null
    ) : TargetNavDest

    data object Back : TargetNavDest
}