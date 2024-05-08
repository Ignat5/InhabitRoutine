package com.ignatlegostaev.inhabitroutine.core.presentation.components.config

sealed interface BaseScreenConfig<out SC : ScreenConfig> {
    data object Idle : BaseScreenConfig<Nothing>
    data class Config<out SC : ScreenConfig>(val config: SC) : BaseScreenConfig<SC>
}