package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface ResetTaskScreenResult : ScreenResult {
    data class Confirm(val taskId: String) : ResetTaskScreenResult
    data object Dismiss : ResetTaskScreenResult
}