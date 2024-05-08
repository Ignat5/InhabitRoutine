package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface DeleteTaskScreenResult : ScreenResult {
    data class Confirm(val taskId: String) : DeleteTaskScreenResult
    data object Dismiss : DeleteTaskScreenResult
}