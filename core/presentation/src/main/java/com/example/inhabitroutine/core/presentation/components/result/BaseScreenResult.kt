package com.example.inhabitroutine.core.presentation.components.result

sealed interface BaseScreenResult<out SR : ScreenResult> {
    data object Idle : BaseScreenResult<Nothing>
    data class Result<out SR : ScreenResult>(val result: SR) : BaseScreenResult<SR>
}