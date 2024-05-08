package com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation

sealed interface BaseScreenNavigation<out SN : ScreenNavigation> {
    data object Idle : BaseScreenNavigation<Nothing>
    data class Destination<out SN : ScreenNavigation>(val destination: SN) : BaseScreenNavigation<SN>
}