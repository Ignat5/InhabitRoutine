package com.example.inhabitroutine.core.presentation.components.result

sealed interface BaseScreenResult<SR : ScreenResult> {
    data object Idle : BaseScreenResult<Nothing>
    data class Result<SR : ScreenResult>(val result: SR) : BaseScreenResult<SR>
}